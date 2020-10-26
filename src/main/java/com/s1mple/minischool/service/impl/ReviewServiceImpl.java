package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.ReviewMapper;
import com.s1mple.minischool.dao.UserMapper;
import com.s1mple.minischool.domain.Vo.ReviewVo;
import com.s1mple.minischool.domain.Review;
import com.s1mple.minischool.service.ReviewService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    UserMapper userMapper;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    @Override
    public List<ReviewVo> getReviewPage(int currentPage, Long trends_id) {
        Page<Review> page = new Page<>(currentPage,10);
        IPage<Review> iPage = reviewMapper.selectPage(page,Wrappers.<Review>lambdaQuery()
                .eq(Review::getTrends_id, trends_id)
                .isNull(Review::getSuperReviewId)
                .orderByDesc(Review::getReviewTime)
        );
        List<Review> reviews = iPage.getRecords();
        List<ReviewVo> reviewVoList = reviews.stream().
                map(review -> {
                    ReviewVo reviewVo = mapper.map(review, ReviewVo.class);
                    reviewVo.setUser(userMapper.selectById(review.getUser_id()));
                    reviewVo.setChildrenReview(reviewMapper.selectList(Wrappers.<Review>lambdaQuery().
                            eq(Review::getSuperReviewId,reviewVo.getReview_id())
                            .orderByAsc(Review::getReviewTime))
                            .stream()
                            .map(review1 -> getReviewVo(review1))
                            .collect(Collectors.toList()));
                    return reviewVo;
                })
                .collect(Collectors.toList());
        return reviewVoList;
    }

    @Override
    public ReviewVo getReviewVo(Review review) {
        ReviewVo reviewVo = mapper.map(review, ReviewVo.class);
        reviewVo.setUser(userMapper.selectById(review.getUser_id()));
        if (!ObjectUtils.isEmpty(review.getBroReviewId())){
            reviewVo.setBroReview(getReviewVo(reviewMapper.selectById(review.getBroReviewId())));
        }
        return reviewVo;
    }
}
