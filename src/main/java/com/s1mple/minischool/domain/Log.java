package com.s1mple.minischool.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    @TableId
    private Long log_id;

    private Integer type;

    private String method;

    private String content;

    private String params;

    private Long opt_id;

    private String ip;

    private String exception;

    private Date time;

}
