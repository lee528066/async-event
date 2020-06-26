package com.lee.async.event.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liwei
 * @date 2020/06/26
 */
public class AsyncEventLogger {

    private static final Logger logger = LoggerFactory.getLogger("AsyncEventLogger");

    public static Logger getLogger() {
        return logger;
    }
}
