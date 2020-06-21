package com.lee.async.event.core.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author liwei
 * @date 2020/06/02
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD})
@Inherited
public @interface AsyncEvent {
    /**
     * 事件的描述
     */
    String name();

    /**
     * @return
     */
    String uniqueKey();

    /**
     * 是否重入
     * 如果某个被AsyncEvent修饰的方法，被另一个AsyncEvent修饰的方法中调用到的时候：
     * 当skipAsyncEventAop为true：直接执行该方法
     * 当skipAsyncEventAop为false：按照AsyncEventAspect的逻辑，先把event持久化，再通过消息执行
     */
    boolean skipAsyncEventAop() default false;
}
