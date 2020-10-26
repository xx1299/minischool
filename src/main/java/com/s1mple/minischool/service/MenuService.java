package com.s1mple.minischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.s1mple.minischool.domain.Menu;
import com.s1mple.minischool.domain.Vo.MenuVo;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<MenuVo> getMenuTree();

    void delMenu(Long menu_id);
}
