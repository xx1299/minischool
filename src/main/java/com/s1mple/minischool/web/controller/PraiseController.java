package com.s1mple.minischool.web.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.s1mple.minischool.domain.po.Praise;
import com.s1mple.minischool.exception.CustomException;
import com.s1mple.minischool.exception.ExceptionType;
import com.s1mple.minischool.service.PraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PraiseController {

    @Autowired
    PraiseService praiseService;

    @PostMapping("/praises/{trends_id}")
    public Praise praise(@PathVariable("trends_id") Long trends_id, HttpServletRequest request){
        System.out.println(trends_id);
        Long user_id = (Long)request.getAttribute("user_id");
        Praise praise = Praise.builder().trends_id(trends_id).user_id(user_id).build();
        if (ObjectUtils.isEmpty(praiseService.getOne(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id,trends_id).eq(Praise::getUser_id,user_id)))){
            praiseService.save(praise);
        }else{
            throw new CustomException(ExceptionType.OTHER_ERROR,"您已经点赞过，再次点击将取消点赞");
        }
        return praise;
    }

    @DeleteMapping("/praises/{trends_id}")
    public void unPraise(@PathVariable("trends_id") Long trends_id, HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        Praise praise = Praise.builder().trends_id(trends_id).user_id(user_id).build();
        praiseService.remove(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id,trends_id).eq(Praise::getUser_id,user_id));
    }
}
