package com.s1mple.minischool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectRoleByAid(Long admin_id);
}
