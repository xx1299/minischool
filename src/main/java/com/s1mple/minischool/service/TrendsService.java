package com.s1mple.minischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.s1mple.minischool.domain.Trends;
import com.s1mple.minischool.domain.Vo.TrendsVo;

import java.io.IOException;

public interface TrendsService extends IService<Trends> {


    public TrendsVo insertTrends(TrendsVo trendsVo) throws IOException;

    public void deleteTrends(Long trends_id);
}
