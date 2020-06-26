package com.lee.async.event.core.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.lee.async.event.common.enums.EventStatus;
import com.lee.async.event.common.logger.AsyncEventLogger;
import com.lee.async.event.core.EventHandler;
import com.lee.async.event.core.EventSchema;
import com.lee.async.event.core.ParamPair;
import com.lee.async.event.core.anno.AsyncEvent;
import com.lee.async.event.core.convertor.EventConvertor;
import com.lee.async.event.core.producer.AsyncEventProducer;
import com.lee.async.event.core.service.EventService;
import com.lee.async.event.dao.entity.EventDO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author liwei
 * @date 2020/06/02
 */
@Component
@Aspect
public class AsyncEventAspect {

    private static final Logger log = AsyncEventLogger.getLogger();

    public static ThreadLocal<String> selfTag = new ThreadLocal<>();

    @Pointcut("@annotation(com.lee.async.event.core.anno.AsyncEvent)")
    public void SyncEventAnnotation() {

    }

    @Resource
    private EventHandler eventHandler;

    @Resource
    private EventConvertor eventConvertor;

    @Resource
    private EventService eventService;

    @Resource
    private AsyncEventProducer asyncEventProducer;

    /**
     * 用于SpEL表达式解析.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 用于获取方法参数定义名字.
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around(value = "SyncEventAnnotation() && @annotation(asyncEvent)")
    public void doAround(ProceedingJoinPoint pjp, AsyncEvent asyncEvent) throws Throwable {
        EventSchema eventSchema = buildEventSchema(pjp, asyncEvent);
        EventDO exist = eventService.getByUk(eventSchema.getUniqueKey());
        String selfTag = AsyncEventAspect.selfTag.get();
        if (asyncEvent.skipAsyncEventAop() || (StringUtils.isNotBlank(selfTag) && selfTag.equals(eventSchema.getUniqueKey()))) {
            //消息消费端触发
            if(Objects.nonNull(exist) && isSentOrFailEvent(exist)){
                try {
                    long start = System.currentTimeMillis();
                    pjp.proceed();
                    log.info(String.format("event[%s]执行耗时：%d ms", eventSchema.getUniqueKey(), System.currentTimeMillis() - start));
                }catch (Exception e){
                    log.error(String.format("event[%s]执行发生异常:", eventSchema.getUniqueKey()), e);
                    eventService.fail(exist.getId(), exist.getFailCount(), getErrorReason(e));
                    //异常抛出触发消息重复消费
                    throw e;
                }
                eventService.finish(exist.getId());
            }
        }else{
            //消息发送端触发
            if (exist != null) {
                if (exist.getStatus() == EventStatus.CREATE.getCode()) {
                    boolean sendSuccess = asyncEventProducer.sendEvent(eventSchema);
                    if (sendSuccess) {
                        eventService.send(exist.getId());
                    }
                } else if (exist.getStatus() == EventStatus.FINISH.getCode()) {
                    log.info(String.format("event[%s]已经执行-幂等", eventSchema.getUniqueKey()));
                }
            } else {
                EventDO eventDO = eventConvertor.schemaToDO(eventSchema);
                eventService.add(eventDO);
                boolean sendSuccess = asyncEventProducer.sendEvent(eventSchema);
                if (sendSuccess) {
                    eventService.send(eventDO.getId());
                }
            }
        }
    }

    private boolean isSentOrFailEvent(EventDO exist) {
        return exist.getStatus() == EventStatus.SENT.getCode() || exist.getStatus() == EventStatus.FAIL.getCode();
    }

    private String getErrorReason(Exception e) {
        return StringUtils.isEmpty(e.getMessage()) ? e.getCause().toString().substring(0, 2000)
            : e.getMessage();
    }

    private void processEvent(EventSchema eventSchema, EventDO exist) throws Exception {
        try {
            eventHandler.process(eventSchema);
            eventService.finish(exist.getId());
        } catch (Throwable e) {
            log.info("执行失败", e);

            throw e;
        }
    }

    private EventSchema buildEventSchema(ProceedingJoinPoint pjp, AsyncEvent asyncEvent) throws Exception {
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        Method method = signature.getMethod();
        Class<?> beanClass = getBeanClass(pjp);
        Assert.isTrue(method.getReturnType().equals(Void.TYPE),
            beanClass.getName() + "#" + method.getName() + "返回值不为void");
        String billContent = generateKeyBySpEL(asyncEvent.uniqueKey(), pjp);
        EventSchema eventSchema = new EventSchema();
        eventSchema.setBillContent(billContent);
        eventSchema.setUniqueKey(getUniqueKey(method, beanClass, billContent));
        eventSchema.setMethodName(method.getName());
        eventSchema.setName(asyncEvent.name());
        eventSchema.setBeanClass(beanClass);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = pjp.getArgs();
        List<ParamPair> paramPairList = Lists.newArrayListWithCapacity(parameterTypes.length);
        for(int i = 0; i< parameterTypes.length; i++){
            paramPairList.add(new ParamPair(parameterTypes[i].getName(), JSON.toJSONString(args[i])));
        }
        eventSchema.setParamPairs(paramPairList);
        return eventSchema;
    }

    private String getUniqueKey(Method method, Class<?> beanClass, String billContent) {
        return DigestUtils.md5Hex(billContent + ":" + beanClass.getName() + ":" + method.getName());
    }

    /**
     * 获取bean的原始类名称
     */
    private Class<?> getBeanClass(ProceedingJoinPoint pjp) throws Exception {
        Class<?> beanClass = null;
        Object nowObject = pjp.getThis();
        if (AopUtils.isAopProxy(nowObject)) {
            if (AopUtils.isJdkDynamicProxy(nowObject)) {
                beanClass = getJdkDynamicProxyTargetClass(nowObject);
            } else { //cglib
                beanClass = getCglibProxyTargetClass(nowObject);
            }
        } else {
            beanClass = nowObject.getClass();
        }
        return beanClass;
    }

    public String generateKeyBySpEL(String spELString, ProceedingJoinPoint joinPoint) {
        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 使用spring的DefaultParameterNameDiscoverer获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        // 解析过后的Spring表达式对象
        Expression expression = parser.parseExpression(spELString);
        // spring的表达式上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        // 通过joinPoint获取被注解方法的形参
        Object[] args = joinPoint.getArgs();
        // 给上下文赋值
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        // 表达式从上下文中计算出实际参数值
        /*如:
            @annotation(key="#student.name")
             method(Student student)
             那么就可以解析出方法形参的某属性值，return “xiaoming”;
          */
        return expression.getValue(context).toString();
    }

    private Class<?> getCglibProxyTargetClass(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        Assert.notNull(target, "getCglibProxyTargetClass target为null");
        return target.getClass();
    }

    private Class<?> getJdkDynamicProxyTargetClass(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy)h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
        Assert.notNull(target, "getJdkDynamicProxyTargetClass target为null");
        return target.getClass();
    }
}
