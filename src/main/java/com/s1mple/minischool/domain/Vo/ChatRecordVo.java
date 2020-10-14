package com.s1mple.minischool.domain.Vo;

import com.s1mple.minischool.domain.Message;
import com.s1mple.minischool.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecordVo {

    Message lastMessage;

    User chatUser;

    Integer unReciveCount;

}
