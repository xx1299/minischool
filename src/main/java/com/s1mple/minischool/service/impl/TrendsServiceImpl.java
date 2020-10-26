package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.*;
import com.s1mple.minischool.domain.Praise;
import com.s1mple.minischool.domain.Review;
import com.s1mple.minischool.domain.Trends;
import com.s1mple.minischool.domain.Trendsimg;
import com.s1mple.minischool.domain.Vo.TrendsVo;
import com.s1mple.minischool.service.TrendsService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrendsServiceImpl extends ServiceImpl<TrendsMapper, Trends> implements TrendsService {

    @Autowired
    TrendsMapper trendsMapper;

    @Autowired
    TrendsImgMapper imgMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    PraiseMapper praiseMapper;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();


    @Override
    public TrendsVo insertTrends(TrendsVo trendsVo) throws IOException {

        Trends trendsPo = mapper.map(trendsVo, Trends.class);
        trendsPo.setUser_id(trendsVo.getUser().getUser_id());
        trendsPo.setReleaseTime(new Date(System.currentTimeMillis()));
        trendsMapper.insert(trendsPo);
        List<Trendsimg> imgs = trendsVo.getTrendsimg().stream()
                .map(img -> Trendsimg.builder().img_id(img.getImg_id()).tid(trendsPo.getTrends_id()).build())
                .collect(Collectors.toList());
        imgs.forEach(value->imgMapper.updateById(value));
        return trendsVo;
    }

    @Override
    public void deleteTrends(Long trends_id) {
        imgMapper.delete(Wrappers.<Trendsimg>lambdaQuery().eq(Trendsimg::getTid, trends_id));
        reviewMapper.delete(Wrappers.<Review>lambdaQuery().eq(Review::getTrends_id,trends_id));
        praiseMapper.delete(Wrappers.<Praise>lambdaQuery().eq(Praise::getTrends_id, trends_id));
        trendsMapper.deleteById(trends_id);
        return;
    }




}
