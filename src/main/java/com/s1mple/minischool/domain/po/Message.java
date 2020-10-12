package com.s1mple.minischool.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    @TableId
    private Long message_id;

    private Long send_id;

    private Long receive_id;

    private Date sendTime;

    private String content;

    /*表示是否被用户接收 0代表已接收，1代表未接收*/
    private Integer state;
}
