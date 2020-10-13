package com.s1mple.minischool;

import com.s1mple.minischool.dao.ReviewMapper;
import com.s1mple.minischool.dao.TrendsMapper;
import com.s1mple.minischool.dao.UserMapper;
import com.s1mple.minischool.domain.Praise;
import com.s1mple.minischool.service.PraiseService;
import com.s1mple.minischool.service.ReviewService;
import com.s1mple.minischool.service.TrendsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class MinischoolApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TrendsService trendsService;

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    TrendsMapper trendsMapper;

    @Autowired
    ReviewService reviewSerview;

    @Autowired
    PraiseService praiseService;


    @Test
    void contextLoads() {
        Praise build = Praise.builder().user_id(1310429363715567618L).trends_id(1310437726570532866L).build();
        System.out.println(new Date(System.currentTimeMillis()));
//        praiseService.save(build);
//        praiseService.remove(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id,build.getTrends_id()).eq(Praise::getUser_id,build.getUser_id()));
    }

}
