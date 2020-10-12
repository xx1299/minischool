package com.s1mple.minischool.domain.Vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.s1mple.minischool.domain.po.Review;
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
public class ReviewVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long review_id;

    private User user;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long trends_id;

    private String content;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long reviewTime;

    private List<ReviewVo> childrenReview = new ArrayList<>();

    private ReviewVo broReview;

}
