package com.lee.async.event.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author liwei
 * @date 2020/06/21
 */
@Configuration
@PropertySource("classpath:async-event-application.properties")
@MapperScan("com.lee.async.event.dao.mapper")
public class AsyncEventAutoConfiguration {

}
