package com.s1mple.minischool.domain.Vo;

import com.s1mple.minischool.domain.po.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private String token;

    private Boolean state;

    private String avatarUrl;

    private String nickName;

    private String city;

    private String sex;

    private String school;

    private String signature;

}
