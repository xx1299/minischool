package com.s1mple.minischool.domain.Vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long user_id;

    private String token;

    private Boolean state;

    private String avatarUrl;

    private String nickName;

    private String city;

    private String sex;

    private String school;

    private String signature;

}
