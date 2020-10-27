package com.s1mple.minischool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<MenuVo> selectMenuTree();

    List<Menu> selectChildrenMenu(@Param("parent_id") Long parent_id);
}
