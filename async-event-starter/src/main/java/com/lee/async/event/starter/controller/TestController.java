package com.lee.async.event.starter.controller;

import javax.annotation.Resource;

import com.lee.async.event.core.demo.DemoService;
import com.lee.async.event.core.demo.DemoService.Param;
import com.lee.async.event.core.service.impl.TransactionOutService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liwei
 * @date 2020/06/02
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private DemoService demoService;
    @Resource
    private TransactionOutService transactionOutService;

    @GetMapping("/demo")
    public String test(long id, String name){
        Param param = new Param();
        param.setId(id);
        param.setName(name);
        demoService.sayHello(param);
        return "success";
    }


    @GetMapping("/testTransaction")
    public int testTransaction(int id){
        return transactionOutService.entryTransaction(id);
    }
}
