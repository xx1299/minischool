package com.s1mple.minischool.web.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.s1mple.minischool.domain.Praise;
import com.s1mple.minischool.exception.CustomException;
import com.s1mple.minischool.exception.ExceptionType;
import com.s1mple.minischool.service.PraiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(tags = "动态点赞模块", value = "点赞，取消点赞")
public class PraiseController {

    @Autowired
    PraiseService praiseService;

    @ApiOperation("点赞动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trends_id",value = "动态Id",required = true,paramType = "path",dataType = "Long"),
    })
    @PostMapping("/praises/{trends_id}")
    public Praise praise(@PathVariable("trends_id") Long trends_id, HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        Praise praise = Praise.builder().trends_id(trends_id).user_id(user_id).build();
        if (ObjectUtils.isEmpty(praiseService.getOne(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id,trends_id).eq(Praise::getUser_id,user_id)))){
            praiseService.save(praise);
        }else{
            throw new CustomException(ExceptionType.OTHER_ERROR,"您已经点赞过，再次点击将取消点赞");
        }
        return praise;
    }

    @ApiOperation("取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trends_id",value = "动态Id",required = true,paramType = "path",dataType = "Long"),
    })
    @DeleteMapping("/praises/{trends_id}")
    public void unPraise(@PathVariable("trends_id") Long trends_id, HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        Praise praise = Praise.builder().trends_id(trends_id).user_id(user_id).build();
        praiseService.remove(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id,trends_id).eq(Praise::getUser_id,user_id));
    }
}
