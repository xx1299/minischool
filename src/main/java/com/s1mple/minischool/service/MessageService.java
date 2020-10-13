package com.s1mple.minischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.domain.Message;

import java.util.List;
import java.util.Map;

public interface MessageService extends IService<Message> {
    Map<Long, List<Message>> notReviceMessage(Long user_id);

    List<MessageVo> withUserChatRecord(Long user_id, Long chat_user_id);

    Integer notReviceMessageCount(Long user_id);
}
