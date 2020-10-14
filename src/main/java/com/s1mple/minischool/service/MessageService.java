package com.s1mple.minischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.s1mple.minischool.domain.Vo.ChatRecordVo;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.domain.Message;

import java.util.List;
import java.util.Map;

public interface MessageService extends IService<Message> {

    List<MessageVo> withUserChatRecord(Long user_id, Long chat_user_id);

    Integer notReviceMessageCount(Long user_id);

    void deleteChatRecord(Long user_id, Long chat_user_id);

    List<ChatRecordVo> getAllChatUserLastRecord(Long user_id);
}
