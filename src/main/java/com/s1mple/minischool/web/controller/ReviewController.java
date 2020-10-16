package com.s1mple.minischool.web.controller;

import com.s1mple.minischool.domain.Vo.ReviewVo;
import com.s1mple.minischool.domain.Review;
import com.s1mple.minischool.service.ReviewService;
import com.s1mple.minischool.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "动态评论模块", value = "获取某篇动态评论，对动态进行评论")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    @ApiOperation("分页获取动态回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数",required = true,paramType = "form",dataType = "int"),
            @ApiImplicitParam(name = "trends_id",value = "动态Id",required = true,paramType = "form",dataType = "Long"),
    })
    @GetMapping("/reviewPage")
    public List<ReviewVo> getReview(@RequestParam("currentPage") int currentPage, @RequestParam("trends_id") Long trends_id){
        return reviewService.getReviewPage(currentPage,trends_id);
    }

    @ApiOperation("对动态进行评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "review",value = "评论对象",required = true,paramType = "body",dataType = "Review"),
    })
    @PostMapping("/review")
    public ReviewVo addReview(@RequestBody Review review, HttpServletRequest request){
        Long user_id = (Long) request.getAttribute("user_id");
        review.setUser_id(user_id);
        review.setReviewTime(new Date(System.currentTimeMillis()));
        reviewService.save(review);
        ReviewVo reviewVo = reviewService.getReviewVo(review);
        return reviewVo;
    }



}
