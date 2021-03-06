package com.lee.async.event.core.demo;

import javax.annotation.Resource;

import com.lee.async.event.core.anno.AsyncEvent;
import com.lee.async.event.core.anno.TestLog;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @author liwei
 * @date 2020/06/02
 */
@Service
public class DemoService {

    @Resource
    private DemoService2 demoService2;

    @AsyncEvent(uniqueKey = "#param.id+'#'+#param.name", name = "测试方法")
    public void sayHello(Param param) {
        System.out.println("DemoService:" + param.toString());
        demoService2.sayHello2(param);
    }

    @Data
    public static class Param {
        private long id;
        private String name;
    }
}
