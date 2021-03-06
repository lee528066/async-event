package com.lee.async.event.core.concumer;

import javax.annotation.Resource;

import com.lee.async.event.core.EventHandler;
import com.lee.async.event.core.EventSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author liwei
 * @date 2020/06/20
 */
@Slf4j
@Component
@RocketMQMessageListener(
    nameServer="${spring.rocketmq.nameServer}",
    topic = "${aysnc.event.topic}",
    consumerGroup = "${spring.rocketmq.async.event.group}")
public class AsyncEventConsumer implements RocketMQListener<EventSchema> {

    @Resource
    private EventHandler eventHandler;

    @Override
    public void onMessage(EventSchema eventSchema) {
        log.info(String.format("event[%s]接收消息", eventSchema.getUniqueKey()));
        eventHandler.process(eventSchema);
    }
}
