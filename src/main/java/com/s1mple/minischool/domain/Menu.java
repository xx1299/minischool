package com.s1mple.minischool.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    @TableId
    private Long menu_id;

    private String title;

    private String path;

    private Long parent_id;


}
