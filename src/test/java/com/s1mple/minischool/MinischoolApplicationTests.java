package com.s1mple.minischool;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.s1mple.minischool.dao.ReviewMapper;
import com.s1mple.minischool.dao.TrendsMapper;
import com.s1mple.minischool.dao.UserMapper;
import com.s1mple.minischool.domain.Vo.ReviewVo;
import com.s1mple.minischool.domain.po.Praise;
import com.s1mple.minischool.domain.po.Review;
import com.s1mple.minischool.domain.po.User;
import com.s1mple.minischool.service.PraiseService;
import com.s1mple.minischool.service.ReviewService;
import com.s1mple.minischool.service.TrendsService;
import com.s1mple.minischool.service.UserService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
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
