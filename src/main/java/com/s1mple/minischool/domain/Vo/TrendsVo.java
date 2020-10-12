package com.s1mple.minischool.domain.Vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.s1mple.minischool.domain.po.Trendsimg;
import com.s1mple.minischool.domain.po.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrendsVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long trends_id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long releaseTime;

    private String content;

    private String releaseSchool;

    private User user;

    private List<Trendsimg> trendsimg = new ArrayList<>();

    private Integer praise;

    private Integer review;

    private Boolean isPraise;

}
