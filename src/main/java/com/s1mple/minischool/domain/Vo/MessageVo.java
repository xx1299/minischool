package com.s1mple.minischool.domain.Vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.s1mple.minischool.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo{

    private Long message_id;

    private User sendUser;

    private User receiveUser;
    @JsonSerialize(using = ToStringSerializer.class)
    private Date sendTime;

    private String content;

    private Integer noRevice;


}
