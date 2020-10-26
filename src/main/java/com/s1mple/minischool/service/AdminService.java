package com.s1mple.minischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.s1mple.minischool.domain.Admin;
import com.s1mple.minischool.domain.Role;

public interface AdminService extends IService<Admin> {
    boolean check(Admin admin);
}
