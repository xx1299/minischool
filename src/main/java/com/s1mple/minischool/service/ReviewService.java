package com.s1mple.minischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.s1mple.minischool.domain.Vo.ReviewVo;
import com.s1mple.minischool.domain.po.Review;

import java.util.List;

public interface ReviewService extends IService<Review> {
    List<ReviewVo> getReviewPage(int currentPage, Long trends_id);

    ReviewVo getReviewVo(Review review);
}
