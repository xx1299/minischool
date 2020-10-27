package com.s1mple.minischool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AuthorityMapper extends BaseMapper<Authority> {
    List<Authority> selectAuthorityByRid(List<Role> roles);
}
