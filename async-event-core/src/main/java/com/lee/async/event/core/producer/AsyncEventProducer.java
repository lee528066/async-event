package com.lee.async.event.core.producer;

import com.lee.async.event.core.EventSchema;

/**
 * @author liwei
 * @date 2020/06/20
 */
public interface AsyncEventProducer {
    boolean sendEvent(EventSchema eventSchema);
}
