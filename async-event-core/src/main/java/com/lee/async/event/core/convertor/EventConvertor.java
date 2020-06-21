package com.lee.async.event.core.convertor;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.lee.async.event.common.enums.EventStatus;
import com.lee.async.event.core.EventSchema;
import com.lee.async.event.core.constant.StringConstant;
import com.lee.async.event.dao.entity.EventDO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author liwei
 * @date 2020/06/16
 */
@Component
public class EventConvertor {

    public EventDO schemaToDO(EventSchema eventSchema){
        EventDO eventDO = new EventDO();
        eventDO.setUniqueKey(eventSchema.getUniqueKey());
        eventDO.setBeanClass(eventSchema.getBeanClass().getName());
        eventDO.setName(eventSchema.getName());
        eventDO.setMethodName(eventSchema.getMethodName());
        eventDO.setBillContent(eventSchema.getBillContent());
        eventDO.setStatus(EventStatus.CREATE.getCode());
        eventDO.setParamPairs(JSON.toJSONString(eventSchema.getParamPairs()));
        return eventDO;
    }

    private String getParamTypesStr(Class<?>[] paramTypes){
        Assert.notEmpty(paramTypes, "paramTypes为空");
        return Arrays.stream(paramTypes).map(Class::getName)
            .collect(Collectors.joining(StringConstant.PARAM_TYPES_SPILT));
    }

    private String getParamValues(Object[] paramValues){
        Assert.notEmpty(paramValues, "paramValues为空");
        return JSONObject.toJSONString(paramValues);
    }
}
