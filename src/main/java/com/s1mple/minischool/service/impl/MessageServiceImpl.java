package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.MessageMapper;
import com.s1mple.minischool.dao.UserMapper;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.domain.po.Message;
import com.s1mple.minischool.service.MessageService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final Integer NO_RECEIVE = 0;

    @Override
    public List<MessageVo> notReviceMessage(Long user_id) {
        List<Message> messages = messageMapper.selectList(Wrappers.<Message>lambdaQuery()
                .eq(Message::getReceive_id, user_id)
                .eq(Message::getState,NO_RECEIVE)
                .orderByDesc(Message::getSendTime)
        );
        List<MessageVo> collect = messages.stream().map(message -> {
            MessageVo messageVo = mapper.map(message, MessageVo.class);
            messageVo.setReciveUser(userMapper.selectById(message.getReceive_id()));
            messageVo.setSendUser(userMapper.selectById(message.getSend_id()));
            return messageVo;
        }).collect(Collectors.toList());
        return collect;
    }

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
            messageVo.setReciveUser(userMapper.selectById(message.getReceive_id()));
            messageVo.setSendUser(userMapper.selectById(message.getSend_id()));
            return messageVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Integer notReviceMessageCount(Long user_id) {
        Integer count = messageMapper.selectCount(Wrappers.<Message>lambdaQuery()
                .eq(Message::getReceive_id, user_id)
                .eq(Message::getState, NO_RECEIVE)
        );
        return count;
    }


}
