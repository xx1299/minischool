package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.MessageMapper;
import com.s1mple.minischool.dao.UserMapper;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.domain.Message;
import com.s1mple.minischool.service.MessageService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    private final Integer RECEIVE = 0;
    private final Integer NO_RECEIVE = 1;

    @Override
    public Map<Long, List<Message>> notReviceMessage(Long user_id) {
        List<Message> noMessages = messageMapper.selectList(Wrappers.<Message>lambdaQuery()
                .eq(Message::getState,NO_RECEIVE)
                .and(wrapper->wrapper.eq(Message::getReceive_id, user_id).or()
                        .eq(Message::getSend_id, user_id))
                .orderByDesc(Message::getSendTime)
        );
        List<Message> messages = messageMapper.selectList(Wrappers.<Message>lambdaQuery()
                .eq(Message::getState,RECEIVE)
                .and(wrapper->wrapper.eq(Message::getReceive_id, user_id).or()
                        .eq(Message::getSend_id, user_id))
                .orderByDesc(Message::getSendTime)
        );
        Map<Long,List<Message>> messageMap = new HashMap<Long,List<Message>>();
        noMessages.stream().forEach(message -> {
            if (user_id.equals(message.getSend_id())) {
                if (!messageMap.containsKey(message.getReceive_id())) {
                    messageMap.put(message.getReceive_id(), new ArrayList<Message>());
                }
                messageMap.get(message.getReceive_id()).add(message);
            } else {
                if (!messageMap.containsKey(message.getSend_id())) {
                    messageMap.get(message.getSend_id()).add(message);
                }
                messageMap.get(message.getSend_id()).add(message);
            }
        });
        messages.stream().forEach(message -> {
            if (user_id.equals(message.getSend_id())) {
                if (!messageMap.containsKey(message.getReceive_id())) {
                    messageMap.put(message.getReceive_id(), new ArrayList<Message>());
                }
                messageMap.get(message.getReceive_id()).add(message);
            } else {
                if (!messageMap.containsKey(message.getSend_id())) {
                    messageMap.put(message.getSend_id(), new ArrayList<Message>());
                }
                messageMap.get(message.getSend_id()).add(message);
            }
        });

//        List<MessageVo> collect = messages.stream().map(message -> {
//            MessageVo messageVo = mapper.map(message, MessageVo.class);
//            messageVo.setReciveUser(userMapper.selectById(message.getReceive_id()));
//            messageVo.setSendUser(userMapper.selectById(message.getSend_id()));
//            return messageVo;
//        }).collect(Collectors.toList());
        System.out.println(messageMap);
        return messageMap;
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
