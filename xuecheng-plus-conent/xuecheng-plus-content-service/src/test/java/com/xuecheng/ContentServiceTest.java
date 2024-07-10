package com.xuecheng;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//内容管理启动类
@EnableFeignClients(basePackages={"com.xuecheng.content.feignclient"})
@SpringBootApplication
public class ContentServiceTest {
    public static void main(String[] args) {
        SpringApplication.run(ContentServiceTest.class,args);
    }
}
