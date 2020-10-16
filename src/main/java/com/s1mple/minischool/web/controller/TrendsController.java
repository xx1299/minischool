package com.s1mple.minischool.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.s1mple.minischool.domain.*;
import com.s1mple.minischool.domain.Vo.TrendsVo;
import com.s1mple.minischool.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(tags = "动态模块", value = "获取动态，获取当前用户动态，获取单个动态，删除动态，上传图片，添加动态")
public class TrendsController {


    @Autowired
    TrendsService trendsService;

    @Autowired
    PraiseService praiseService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @Autowired
    TrendsImgService trendsImgService;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();


    @ApiOperation("分页获取动态数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true,paramType = "form",dataType = "int"),
    })
    @GetMapping("/trendsPage")
    public List<TrendsVo> getTrendsPage(@RequestParam("currentPage") int currentPage,HttpServletRequest request){
        List<Trends> trendsPage = trendsService.page(new Page<Trends>(currentPage,10),Wrappers.<Trends>lambdaQuery().orderByDesc(Trends::getReleaseTime)).getRecords();
        List<TrendsVo> trendsVoList = trendsPage.stream().map(trendsPo -> {
            TrendsVo trendsVo = mapper.map(trendsPo, TrendsVo.class);
            trendsVo.setUser(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUser_id, trendsPo.getUser_id())));
            trendsVo.setReview(reviewService.count(Wrappers.<Review>lambdaQuery().eq(Review::getTrends_id, trendsVo.getTrends_id())));
            trendsVo.setPraise(praiseService.count(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id, trendsVo.getTrends_id())));
            trendsVo.setTrendsimg(trendsImgService.list(Wrappers.<Trendsimg>lambdaQuery().eq(Trendsimg::getTid, trendsVo.getTrends_id())));
            trendsVo.setIsPraise(!ObjectUtils.isEmpty(praiseService.getOne(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id,trendsPo.getTrends_id()).eq(Praise::getUser_id,(Long)request.getAttribute("user_id")))));
            return trendsVo;
        }).collect(Collectors.toList());
        return trendsVoList;
    }

    @ApiOperation("获取当前用户动态")
    @GetMapping("/trends")
    public List<TrendsVo> getTrendsByUid(HttpServletRequest request){
        List<Trends> trendsPage = trendsService.list(Wrappers.<Trends>lambdaQuery().eq(Trends::getUser_id,(Long)request.getAttribute("user_id")).orderByDesc(Trends::getReleaseTime));
        List<TrendsVo> trendsVoList = trendsPage.stream().map(trendsPo -> {
            TrendsVo trendsVo = mapper.map(trendsPo, TrendsVo.class);
            trendsVo.setReview(reviewService.count(Wrappers.<Review>lambdaQuery().eq(Review::getTrends_id, trendsVo.getTrends_id())));
            trendsVo.setPraise(praiseService.count(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id, trendsVo.getTrends_id())));
            trendsVo.setTrendsimg(trendsImgService.list(Wrappers.<Trendsimg>lambdaQuery().eq(Trendsimg::getTid, trendsVo.getTrends_id())));
            return trendsVo;
        }).collect(Collectors.toList());
        return trendsVoList;
    }

    @ApiOperation("获取某个动态数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trends_id",value = "动态Id",required = true,paramType = "path",dataType = "Long"),
    })
    @GetMapping("/trends/{trends_id}")
    public TrendsVo getTrends(@PathVariable("trends_id") Long trends_id){
        Trends trendsPo = trendsService.getOne(Wrappers.<Trends>lambdaQuery().eq(Trends::getTrends_id,trends_id));
        TrendsVo trendsVo = Optional.ofNullable(mapper.map(trendsPo, TrendsVo.class))
                .map(trends -> {
                    trends.setUser(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUser_id, trendsPo.getUser_id())));
                    trends.setTrendsimg(trendsImgService.list(Wrappers.<Trendsimg>lambdaQuery().eq(Trendsimg::getTid, trends.getTrends_id())));
                    return trends;
                }).get();
        return trendsVo;
    }

    @ApiOperation("添加动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trendsVo",value = "动态对象",required = true,paramType = "body",dataType = "TrendVo"),
    })
    @PostMapping("/trends")
    public TrendsVo addTrends(@RequestBody TrendsVo trendsVo, HttpServletRequest request) throws IOException {
        Long user_id = (Long)request.getAttribute("user_id");
        trendsVo.setUser(User.builder().user_id(user_id).build());
        return trendsService.insertTrends(trendsVo);
    }

    @ApiOperation("删除动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trends_id",value = "动态Id",required = true,paramType = "path",dataType = "Long"),
    })
    @DeleteMapping("/trends/{trends_id}")
    public void test(@PathVariable("trends_id") Long trends_id) throws IOException {
        trendsService.deleteTrends(trends_id);
        return ;
    }

    @ApiOperation("上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file",value = "图片对象",required = true,paramType = "form",dataType = "MultipartFile"),
    })
    @PostMapping(value = "/trendsImgs" , produces= "application/json;charset=utf-8")
    public String addImg(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
        String aLong = trendsImgService.insertImg(file,request).toString();
        return aLong;
    }




}
