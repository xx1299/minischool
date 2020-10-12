package com.s1mple.minischool.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @TableId
    private Long review_id;

    private Long user_id;

    private Long trends_id;

    private String content;

    private Date reviewTime;

    private Long superReviewId;

    private Long broReviewId;

}
