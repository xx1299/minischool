package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.AdminMapper;
import com.s1mple.minischool.dao.AuthorityMapper;
import com.s1mple.minischool.domain.Admin;
import com.s1mple.minischool.domain.Authority;
import com.s1mple.minischool.service.AuthorityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements AuthorityService {
}
