package com.lee.async.event.dao.mapper1;

import javax.annotation.Resource;

import com.lee.async.event.core.service.EventService;
import com.lee.async.event.dao.entity.EventDO;
import com.lee.async.event.starter.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author liwei
 * @date 2020/06/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EventServiceTest {

    @Resource
    private EventService eventService;

    @Test
    public void findByUniqueKey() {
    }

    @Test
    public void add() {
    }

    @Test
    public void updateStatus() {
    }

    @Test
    public void finish() {
        EventDO exist = eventService.getById(1);
        eventService.fail(1, exist.getFailCount(),"test errorReason");
    }

    @Test
    public void fail() {
    }
}