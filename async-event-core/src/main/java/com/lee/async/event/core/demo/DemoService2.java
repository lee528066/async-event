package com.lee.async.event.core.demo;

import com.lee.async.event.core.anno.AsyncEvent;
import com.lee.async.event.core.demo.DemoService.Param;
import org.springframework.stereotype.Service;

/**
 * @author liwei
 * @date 2020/06/20
 */
@Service
public class DemoService2 {

    @AsyncEvent(uniqueKey = "#param.id+'#'+#param.name", name = "测试方法2", skipAsyncEventAop = true)
    public void sayHello2(Param param) {
        System.out.println("DemoService2:" + param.toString());
    }
}
