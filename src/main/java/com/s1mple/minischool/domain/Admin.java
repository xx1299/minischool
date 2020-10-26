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
public class Admin {

    @TableId
    private Long admin_id;

    private String username;

    private String password;

    private String avatar;

}
