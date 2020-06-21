package com.lee.async.event.starter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author liwei
 * @date 2020/06/02
 */
@SpringBootApplication(scanBasePackages = {"com.lee.async.event"})
@EnableAspectJAutoProxy
@MapperScan("com.lee.async.event.dao.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
    }
}
