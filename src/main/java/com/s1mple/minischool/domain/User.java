package com.s1mple.minischool.domain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @TableId
    private Long user_id;

    private String openid;

    private String session_key;

    private String avatarUrl;

    private String nickName;

    private String city;

    private Date lastLoginTime;

    private String sex;

    private String school;

    private String signature;

    private Boolean state;

    @TableField(exist = false)
    private String code;
}
