package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.AdminMapper;
import com.s1mple.minischool.dao.MessageMapper;
import com.s1mple.minischool.domain.Admin;
import com.s1mple.minischool.domain.Message;
import com.s1mple.minischool.exception.CustomException;
import com.s1mple.minischool.exception.ExceptionType;
import com.s1mple.minischool.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public boolean check(Admin admin) {

        List<Admin> admins = adminMapper.selectList(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, admin.getUsername()));
        if (admins.size()>0) {
            Admin checkAdmin = admins.get(0);
            if (checkAdmin.getPassword().equals(admin.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
