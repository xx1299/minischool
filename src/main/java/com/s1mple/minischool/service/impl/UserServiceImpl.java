package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.UserMapper;
import com.s1mple.minischool.domain.User;
import com.s1mple.minischool.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {



}
