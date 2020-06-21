package com.lee.async.event.core.aspect;

import com.lee.async.event.core.anno.AsyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author liwei
 * @date 2020/06/02
 */
@Component
@Aspect
@Slf4j
public class TestLogAspect {

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
