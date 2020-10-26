package com.s1mple.minischool.web.controller;

import com.s1mple.minischool.domain.Admin;
import com.s1mple.minischool.domain.AjxsResponse;
import com.s1mple.minischool.exception.CustomException;
import com.s1mple.minischool.exception.ExceptionType;
import com.s1mple.minischool.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {


    @Autowired
    AdminService adminService;

    @PostMapping("/login")
    @CrossOrigin
    public AjxsResponse login(@RequestBody Admin admin){
        System.out.println(admin);
        if (adminService.check(admin)){
            log.info("管理员{}登录后台管理系统成功",admin.getUsername());
            return AjxsResponse.success("登录成功",admin);
        }
        log.error("用户名或密码输入错误,登录失败");
        return AjxsResponse.error(new CustomException(ExceptionType.User_INPUT_ERROR,"用户名或密码输入错误"));
    }

}
