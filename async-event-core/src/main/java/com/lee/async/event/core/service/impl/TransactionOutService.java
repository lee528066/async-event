package com.lee.async.event.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liwei
 * @date 2020/09/21
 */
@Service
public class TransactionOutService {

    @Resource
    private TransactionCoreService transactionCoreService;

    @Transactional
    public int entryTransaction(int id){
        int count = transactionCoreService.doTransaction(id);
        if (id == 1) {
            throw new RuntimeException("test error");
        }
        return count;
    }
}
