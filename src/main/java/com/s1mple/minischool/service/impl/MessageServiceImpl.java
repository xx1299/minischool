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

import java.util.*;
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
            message.setState(RECEIVE);
            MessageVo messageVo = mapper.map(message, MessageVo.class);
            messageVo.setReceiveUser(userMapper.selectById(message.getReceive_id()));
            messageVo.setSendUser(userMapper.selectById(message.getSend_id()));
            messageMapper.updateById(message);
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
        Map<Long,Message> map = new HashMap<>();
        messages.forEach(it -> {
            if (it.getSend_id().equals(user_id)){
                if (map.isEmpty()){
                    map.put(it.getReceive_id(),it);
                }else{
                    if (map.containsKey(it.getReceive_id())){
                        if (map.get(it.getReceive_id()).getSendTime().before(it.getSendTime())){
                            map.put(it.getReceive_id(),it);
                        }
                    }else{
                        map.put(it.getReceive_id(),it);
                    }
                }
            }else{
                if (map.isEmpty()){
                    map.put(it.getSend_id(),it);
                }else{
                    if (map.containsKey(it.getSend_id())){
                        if (map.get(it.getSend_id()).getSendTime().before(it.getSendTime())){
                            map.put(it.getSend_id(),it);
                        }
                    }else{
                        map.put(it.getSend_id(),it);
                    }
                }
            }
        });
        Set<Long> longs = map.keySet();
        longs.forEach(it->{
            ChatRecordVo build = ChatRecordVo.builder().chatUser(userMapper.selectById(it)).lastMessage(map.get(it))
                    .unReciveCount(messageMapper.selectCount(Wrappers.<Message>lambdaQuery().eq(Message::getReceive_id, user_id).eq(Message::getSend_id, it).eq(Message::getState, UN_RECEIVE)))
                    .build();
            recordVo.add(build);
        });
        System.out.println(map);
        return recordVo;
    }


}
