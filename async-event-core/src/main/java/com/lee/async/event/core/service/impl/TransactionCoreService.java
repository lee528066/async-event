package com.lee.async.event.core.service.impl;

import javax.annotation.Resource;

import com.lee.async.event.common.enums.EventStatus;
import com.lee.async.event.core.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author liwei
 * @date 2020/09/21
 */
@Service
public class TransactionCoreService {

    @Resource
    private EventService eventService;

    @Transactional
    public int doTransaction(int id){
        int count = eventService.updateStatus(id, EventStatus.CREATE);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                new Thread(()-> System.out.println("update salary success")).start();
            }
        });
        return count;
    }
}
