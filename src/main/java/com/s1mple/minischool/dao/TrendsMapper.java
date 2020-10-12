package com.s1mple.minischool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.s1mple.minischool.domain.po.Trends;
import org.apache.ibatis.annotations.Param;

public interface TrendsMapper extends BaseMapper<Trends> {

    public IPage<Trends> selectTrendsPage(Page<Trends> page);

    public Trends selectTrendsByTid(Long tid);

    public int insertImg(@Param("tid") Long tid, @Param("imgUrl") String imgUrl);

    public int insertTrends(Trends trends);

    public int deleteImgByTid(Long tid);

}
