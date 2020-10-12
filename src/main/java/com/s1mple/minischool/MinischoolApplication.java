package com.s1mple.minischool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.s1mple.minischool.dao")
public class MinischoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinischoolApplication.class, args);
    }

}
