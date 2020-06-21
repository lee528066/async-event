package com.lee.async.event.core.service.impl;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lee.async.event.common.enums.EventStatus;
import com.lee.async.event.core.service.EventService;
import com.lee.async.event.dao.entity.EventDO;
import com.lee.async.event.dao.mapper.EventMapper;
import org.springframework.stereotype.Service;

/**
 * @author liwei
 * @date 2020/06/17
 */
@Service
public class EventServiceImpl implements EventService {

    @Resource
    private EventMapper eventMapper;

    @Override
    public EventDO getById(long id) {
        return eventMapper.selectById(id);
    }

    @Override
    public EventDO getByUk(String uniqueKey) {
        LambdaQueryWrapper<EventDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(EventDO::getUniqueKey, uniqueKey);
        return eventMapper.selectOne(queryWrapper);
    }

    @Override
    public int add(EventDO eventDO) {
        return eventMapper.insert(eventDO);
    }

    @Override
    public int updateStatus(long id, EventStatus status) {
        LambdaUpdateWrapper<EventDO> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(EventDO::getId, id);
        EventDO updateDo = new EventDO();
        updateDo.setStatus(status.getCode());
        return eventMapper.update(updateDo, updateWrapper);
    }

    @Override
    public int finish(long id){
        return updateStatus(id, EventStatus.FINISH);
    }

    @Override
    public int fail(long id, int failCount, String errorReason) {
        LambdaUpdateWrapper<EventDO> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(EventDO::getId, id);
        EventDO updateDo = new EventDO();
        updateDo.setStatus(EventStatus.FAIL.getCode());
        updateDo.setFailCount(failCount + 1);
        updateDo.setErrorReason(errorReason);
        return eventMapper.update(updateDo, updateWrapper);
    }

    @Override
    public int send(long id) {
        return updateStatus(id, EventStatus.SENT);
    }

    @Override
    public int delete(long id) {
        return eventMapper.deleteById(id);
    }

}
