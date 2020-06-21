package com.lee.async.event.core;

import java.util.List;

import lombok.Data;

/**
 * @author liwei
 * @date 2020/06/02
 */
@Data
public class EventSchema {

    /**
     * 唯一主键
     */
    private String uniqueKey;

    /**
     * 业务字段
     */
    private String billContent;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 执行类bean
     */
    private Class<?> beanClass;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 执行方法的参数对
     */
    private List<ParamPair> paramPairs;
}
