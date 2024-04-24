package com.example.yzs;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@org.springframework.boot.autoconfigure.SpringBootApplication()
@EnableScheduling
@ComponentScan(basePackages = {"com.example.yzs", "com.example.yzs.service", "com.example.yzs.controller", "com.example.yzs.config", "com.example.yzs.dao","com.example.yzs.service.impl"})
public class SpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);


    }
}
