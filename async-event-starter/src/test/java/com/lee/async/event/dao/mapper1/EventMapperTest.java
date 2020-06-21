package com.lee.async.event.dao.mapper1;

import java.util.UUID;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.lee.async.event.dao.entity.EventDO;
import com.lee.async.event.dao.mapper.EventMapper;
import com.lee.async.event.starter.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liwei
 * @date 2020/06/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EventMapperTest {

    @Resource
    private EventMapper eventMapper;

    @Test
    public void testEventMapper(){
        EventDO eventDO = new EventDO();
        eventDO.setUniqueKey(UUID.randomUUID().toString());
        eventDO.setBeanClass("com.lee.async.event.dao.mapper.EventMapperTest");
        eventDO.setName("测试");
        eventDO.setMethodName("testEventMapper");
        eventDO.setStatus(0);
        eventDO.setParamPairs("test");
        eventMapper.insert(eventDO);

        EventDO eventDO1 = eventMapper.selectById(1);
        System.out.println(JSON.toJSONString(eventDO1));
    }
}
