package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.PraiseMapper;
import com.s1mple.minischool.domain.Praise;
import com.s1mple.minischool.service.PraiseService;
import org.springframework.stereotype.Service;

@Service
public class PraiseServiceImpl extends ServiceImpl<PraiseMapper, Praise> implements PraiseService {
}
