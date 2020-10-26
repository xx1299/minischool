package com.s1mple.minischool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"com.s1mple.minischool.*","com.s1mple.sys.*"} )
@MapperScan(basePackages = {"com.s1mple.minischool.dao","com.s1mple.sys.dao"})
public class MinischoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinischoolApplication.class, args);
    }

}
