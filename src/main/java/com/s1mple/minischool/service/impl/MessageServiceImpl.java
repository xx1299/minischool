package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.MessageMapper;
import com.s1mple.minischool.dao.UserMapper;
import com.s1mple.minischool.domain.Message;
import com.s1mple.minischool.domain.Vo.ChatRecordVo;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.service.MessageService;
import com.s1mple.minischool.service.UserService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    private final Integer RECEIVE = 1;
    private final Integer UN_RECEIVE = 0;

    @Override
    public List<MessageVo> withUserChatRecord(Long user_id, Long chat_user_id) {

        List<Message> messages = messageMapper.selectList(Wrappers.<Message>lambdaQuery()
                .eq(Message::getSend_id, user_id)
                .eq(Message::getReceive_id, chat_user_id)
                .or()
                .eq(Message::getSend_id, chat_user_id)
                .eq(Message::getReceive_id, user_id)
                .orderByDesc(Message::getSendTime)
        );
        List<MessageVo> collect = messages.stream().map(message -> {
            MessageVo messageVo = mapper.map(message, MessageVo.class);
            messageVo.setReceiveUser(userMapper.selectById(message.getReceive_id()));
            messageVo.setSendUser(userMapper.selectById(message.getSend_id()));
            return messageVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Integer notReviceMessageCount(Long user_id) {
        Integer count = messageMapper.selectCount(Wrappers.<Message>lambdaQuery()
                .eq(Message::getReceive_id, user_id)
                .eq(Message::getState, UN_RECEIVE)
        );
        return count;
    }

    @Override
    public void deleteChatRecord(Long user_id, Long chat_user_id) {
        messageMapper.delete(Wrappers.<Message>lambdaQuery()
                .eq(Message::getSend_id, user_id)
                .eq(Message::getReceive_id, chat_user_id)
                .or()
                .eq(Message::getSend_id, chat_user_id)
                .eq(Message::getReceive_id, user_id)
        );
    }

    @Override
    public List<ChatRecordVo> getAllChatUserLastRecord(Long user_id) {
        List<Message> messages = messageMapper.selectList(Wrappers.<Message>lambdaQuery()
                .eq(Message::getSend_id, user_id)
                .or()
                .eq(Message::getReceive_id, user_id)
                .orderByDesc(Message::getSendTime)
        );
        List<ChatRecordVo> recordVo = new ArrayList<>();
        messages.forEach(it->{
            if (recordVo.isEmpty()){
                ChatRecordVo build = ChatRecordVo.builder().chatUser(userMapper.selectById(it.getReceive_id())).lastMessage(it).build();
                if (it.getState().equals(UN_RECEIVE)){
                    build.setUnReciveCount(1);
                }
                recordVo.add(build);
            }
            if (it.getSend_id().equals(user_id)){
                recordVo.forEach(va->{
                    if (it.getReceive_id().equals(va.getChatUser().getUser_id())){
                        if (it.getState().equals(UN_RECEIVE)){
                            va.setUnReciveCount(va.getUnReciveCount()+1);
                        }
                        if (!va.getLastMessage().getSendTime().before(it.getSendTime())){
                            va.setLastMessage(it);
                        }
                    }else{
                        ChatRecordVo build = ChatRecordVo.builder().chatUser(userMapper.selectById(it.getReceive_id())).lastMessage(it).build();
                        if (it.getState().equals(UN_RECEIVE)){
                            build.setUnReciveCount(1);
                        }
                        recordVo.add(build);
                    }
                });
            }else{
                recordVo.forEach(va->{
                    if (it.getSend_id().equals(va.getChatUser().getUser_id())){
                        if (it.getState().equals(UN_RECEIVE)){
                            va.setUnReciveCount(va.getUnReciveCount()+1);
                        }
                        if (!va.getLastMessage().getSendTime().before(it.getSendTime())){
                            va.setLastMessage(it);
                        }
                    }else{
                        ChatRecordVo build = ChatRecordVo.builder().chatUser(userMapper.selectById(it.getSend_id())).lastMessage(it).build();
                        if (it.getState().equals(UN_RECEIVE)){
                            build.setUnReciveCount(1);
                        }
                        recordVo.add(build);
                    }
                });
            }
        });
        return recordVo;
    }


}
