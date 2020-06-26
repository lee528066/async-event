package com.lee.async.event.core;

import java.util.List;

/**
 * @author liwei
 * @date 2020/06/02
 */
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

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getBillContent() {
        return billContent;
    }

    public void setBillContent(String billContent) {
        this.billContent = billContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ParamPair> getParamPairs() {
        return paramPairs;
    }

    public void setParamPairs(List<ParamPair> paramPairs) {
        this.paramPairs = paramPairs;
    }
}
