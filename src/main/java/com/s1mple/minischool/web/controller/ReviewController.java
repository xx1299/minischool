package com.s1mple.minischool.web.controller;

import com.s1mple.minischool.domain.Vo.ReviewVo;
import com.s1mple.minischool.domain.Review;
import com.s1mple.minischool.service.ReviewService;
import com.s1mple.minischool.service.UserService;
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
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    @GetMapping("/reviewPage")
    public List<ReviewVo> getReview(@RequestParam("currentPage") int currentPage, @RequestParam("trends_id") Long trends_id){
        return reviewService.getReviewPage(currentPage,trends_id);
    }

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
