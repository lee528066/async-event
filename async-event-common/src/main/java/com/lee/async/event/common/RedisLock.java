package com.lee.async.event.common;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liwei
 * @date 2020/07/21
 */
@Component
public class RedisLock {

    private RedissonClient redissonClient;

    @Value("${spring.redis.serverUrl}")
    private String redisUrl;

    @PostConstruct
    private void init() {
        if(redissonClient != null){
            return;
        }
        //配置config，如果是单机版的redis，那么就是使用config.useSingleServer()
        //如果是集群，那么请使用config.useClusterServers()
        Config config = new Config();
        config.useSingleServer().setAddress(redisUrl);
        redissonClient = Redisson.create(config);
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 加锁
     * @return
     */
    public void lock(String lockKey){
        redissonClient.getLock(lockKey).lock();
    }

    /**
     * 释放锁
     */
    public void unLock(String lockKey){
        redissonClient.getLock(lockKey).unlock();
    }

    /**
     * 带超时的加锁
     * @param lockKey
     * @param tomeout 秒为单位
     */
    public void lock(String lockKey, Long tomeout){
        redissonClient.getLock(lockKey).lock(tomeout, TimeUnit.SECONDS);
    }

    /**
     * 带超市的加锁
     * @param lockKey
     * @param unit 时间单位
     * @param tomeout
     */
    public void lock(String lockKey, TimeUnit unit,Long tomeout){
        redissonClient.getLock(lockKey).lock(tomeout, unit);
    }


    /**
     * 尝试获取锁
     * @param lockKey
     * @return
     */
    public boolean tryLock(String lockKey){
        return redissonClient.getLock(lockKey).tryLock();
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param timeout  尝试等待多少秒时间
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String lockKey,Long timeout) throws InterruptedException {
        return redissonClient.getLock(lockKey).tryLock(timeout, TimeUnit.SECONDS);
    }

    /**
     *
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待多久时间
     * @param leaseTime 上锁后多久释放
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) throws InterruptedException {
        return redissonClient.getLock(lockKey).tryLock(waitTime,leaseTime,unit);
    }
}
