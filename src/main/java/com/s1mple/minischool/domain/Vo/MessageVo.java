package com.s1mple.minischool.domain.Vo;

import com.s1mple.minischool.domain.Message;
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

    private User reciveUser;

    private Date sendTime;

    private String content;

    private Integer noRevice;


}
