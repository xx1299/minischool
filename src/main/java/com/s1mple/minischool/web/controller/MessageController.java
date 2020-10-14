package com.s1mple.minischool.web.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1mple.minischool.domain.Message;
import com.s1mple.minischool.domain.Vo.ChatRecordVo;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.exception.CustomException;
import com.s1mple.minischool.exception.ExceptionType;
import com.s1mple.minischool.service.MessageService;
import com.s1mple.minischool.service.UserService;
import com.s1mple.minischool.web.WebSocketServer;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class MessageController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @PutMapping("/sendMessage")
    public MessageVo sendMessage(@RequestParam("receiveId") Long receiveId, @RequestParam("content") String content, HttpServletRequest request) throws IOException, EncodeException {
        Long user_id = (Long)request.getAttribute("user_id");
        if (user_id.equals(receiveId)){
            throw new CustomException(ExceptionType.OTHER_ERROR,"不能向自己发送信息");
        }

        Message build = Message.builder().content(content).receive_id(receiveId).send_id(user_id).sendTime(new Date()).state(0).build();
       if (ObjectUtils.isEmpty(userService.getById(receiveId))||ObjectUtils.isEmpty(receiveId)){
           throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
       }
//       Message build = Message.builder().content(message).receive_id(reciveId).send_id(1310429363715567618L).sendTime(new Date()).state(0).build();
       if (!WebSocketServer.SendOne(receiveId,build)){
           log.info("用户"+receiveId+"不在线，将消息'"+content+"'存入数据库置为未接收状态");
           build.setState(1);;
       }
        messageService.save(build);
        MessageVo build1 = MessageVo.builder().content(content).receiveUser(userService.getById(receiveId)).sendUser(userService.getById(user_id)).sendTime(new Date()).build();
        return build1;
    }

    @GetMapping("/messages/{chat_user_id}")
    public List<MessageVo> userChatRecord(@PathVariable("chat_user_id") Long chat_user_id , HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        if (ObjectUtils.isEmpty(userService.getById(chat_user_id))||ObjectUtils.isEmpty(chat_user_id)){
            throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
        }
        List<MessageVo> messages= messageService.withUserChatRecord(user_id,chat_user_id);
        return messages;
    }

    /**
     * 消息界面数据
     * @param request
     * @return
     */
    @GetMapping("/messages")
    @ResponseBody
    public List<ChatRecordVo> getAllChatUserLastRecord(HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        List<ChatRecordVo> messages= messageService.getAllChatUserLastRecord(user_id);
        return messages;
    }

    @DeleteMapping("/messages/{chat_user_id}")
    @ResponseBody
    public void deleteMessage(@PathVariable("chat_user_id") Long chat_user_id , HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        if (ObjectUtils.isEmpty(userService.getById(chat_user_id))||ObjectUtils.isEmpty(chat_user_id)){
            throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
        }
        messageService.deleteChatRecord(user_id,chat_user_id);
    }


    @GetMapping("/test/{user_id}")
    public List<ChatRecordVo> getAllChatUserLastRecord(@PathVariable("user_id") Long user_id){
        List<ChatRecordVo> messages= messageService.getAllChatUserLastRecord(user_id);
        System.out.println(messages);
        return messages;
    }



}
