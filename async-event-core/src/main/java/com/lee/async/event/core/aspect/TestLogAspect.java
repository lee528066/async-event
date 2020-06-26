package com.lee.async.event.core.aspect;

import com.lee.async.event.common.logger.AsyncEventLogger;
import com.lee.async.event.core.anno.AsyncEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author liwei
 * @date 2020/06/02
 */
@Component
@Aspect
public class TestLogAspect {

    private static final Logger log = AsyncEventLogger.getLogger();

    @Pointcut("@annotation(com.lee.async.event.core.anno.TestLog)")
    public void TestLogAnnotation(){

    }

    @Around(value = "TestLogAnnotation() && @annotation(testLog)")
    public Object doAround(ProceedingJoinPoint pjp, AsyncEvent testLog) throws Throwable{
        log.info("test log before");
        Object proceed = pjp.proceed();
        log.info("test log after");
        return proceed;
    }
}
