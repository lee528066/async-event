package com.lee.async.event.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.fastjson.JSON;

import com.lee.async.event.core.aspect.AsyncEventAspect;
import com.lee.async.event.core.demo.DemoService.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author liwei
 * @date 2020/06/02
 */
@Component
@Slf4j
public class EventHandler implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void process(EventSchema eventSchema){
        Class<?> serviceClass = eventSchema.getBeanClass();
        Object serviceBean = applicationContext.getBean(serviceClass);
        try {
            List<ParamPair> paramPairs = eventSchema.getParamPairs();
            Class<?>[] paramTypes = new Class[paramPairs.size()];
            Object[] paramValues = new Object[paramPairs.size()];
            for (int i = 0; i < paramPairs.size(); i++) {
                paramTypes[i] = Class.forName(paramPairs.get(i).getParamClassStr());
                paramValues[i] = JSON.parseObject(paramPairs.get(i).getParamValueStr(), paramTypes[i]);
            }
            Method method = serviceClass.getMethod(eventSchema.getMethodName(), paramTypes);
            AsyncEventAspect.selfTag.set(eventSchema.getUniqueKey());
            method.invoke(serviceBean, paramValues);
            //TODO
            log.info(String.format("event:[%s]执行成功", eventSchema.getUniqueKey()));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(String.format("event:[%s]执行异常，异常：", eventSchema.getUniqueKey()), e);
        }catch (Exception e){
            log.error("消费发生未知异常", e);
        }finally {
            AsyncEventAspect.selfTag.remove();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
