package com.s1mple.minischool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.s1mple.minischool.domain.po.Review;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewMapper extends BaseMapper<Review> {


}
