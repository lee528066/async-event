package com.lee.async.event.core.service;

import com.lee.async.event.common.enums.EventStatus;
import com.lee.async.event.dao.entity.EventDO;

/**
 * @author liwei
 * @date 2020/06/17
 */
public interface EventService {

    EventDO getById(long id);

    EventDO getByUk(String uniqueKey);

    int add(EventDO eventDO);

    int updateStatus(long id, EventStatus status);

    int finish(long id);

    int fail(long id, int failCount, String errorReason);

    int send(long id);

    int delete(long id);
}
