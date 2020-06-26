package com.lee.async.event.core.producer.impl;

import java.util.Objects;

import javax.annotation.Resource;

import com.lee.async.event.common.logger.AsyncEventLogger;
import com.lee.async.event.core.EventSchema;
import com.lee.async.event.core.producer.AsyncEventProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author liwei
 * @date 2020/06/17
 */
@Component
public class AsyncEventProducerImpl implements AsyncEventProducer {

    private static final Logger log = AsyncEventLogger.getLogger();

    @Resource(name = "baseProducer")
    private RocketMQTemplate baseProducer;

    @Value("${aysnc.event.topic}")
    private String topic;

    public boolean sendEvent(EventSchema eventSchema) {
        Assert.notNull(eventSchema, "eventSchema为空");
        try {
            SendResult result = baseProducer.syncSend(topic, eventSchema);
            log.info(String.format("event[%s]发送消息结果：%s", eventSchema.getUniqueKey(), result));
            if (Objects.nonNull(result) && result.getSendStatus() == SendStatus.SEND_OK) {
                return true;
            }
        } catch (Exception e) {
            log.error(String.format("event[%s]发送消息发生异常:", eventSchema.getUniqueKey()), e);
            throw e;
        }
        return false;
    }
}
