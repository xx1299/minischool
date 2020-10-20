package com.s1mple.minischool.web.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1mple.minischool.domain.Message;
import com.s1mple.minischool.domain.User;
import com.s1mple.minischool.domain.Vo.ChatRecordVo;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.exception.CustomException;
import com.s1mple.minischool.exception.ExceptionType;
import com.s1mple.minischool.service.MessageService;
import com.s1mple.minischool.service.UserService;
import com.s1mple.minischool.utils.DozerUtils;
import com.s1mple.minischool.web.WebSocketServer;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.*;
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
import java.util.Optional;

@RestController
@Slf4j
@Api(tags = "消息模块", value = "发送信息，获得未读消息数，消息界面需要数据，获取消息记录，删除消息记录")
public class MessageController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @ApiOperation("发送信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiveId",value = "接收者Id",required = true,paramType = "body",dataType = "Long"),
            @ApiImplicitParam(name = "content",value = "消息内容",required = true,paramType = "body",dataType = "String")
    })
    @PostMapping("/sendMessage")
    public MessageVo sendMessage(@RequestParam("receiveId") Long receiveId, @RequestParam("content") String content, HttpServletRequest request) throws IOException, EncodeException {
        Long user_id = (Long)request.getAttribute("user_id");
        if (user_id.equals(receiveId)){
            throw new CustomException(ExceptionType.OTHER_ERROR,"不能向自己发送信息");
        }
       if (ObjectUtils.isEmpty(userService.getById(receiveId))||ObjectUtils.isEmpty(receiveId)){
           throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
       }
        Message build = Message.builder().content(content).receive_id(receiveId).send_id(user_id).sendTime(new Date()).state(0).build();
        MessageVo messageVo = Optional.ofNullable(build).map(it -> {
            MessageVo map = DozerUtils.map(build, MessageVo.class);
            map.setSendUser(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUser_id, build.getSend_id())));
            map.setReceiveUser(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUser_id, build.getReceive_id())));
            return map;
        }).get();
        if (!WebSocketServer.SendOne(receiveId,messageVo)){
           log.info("用户"+receiveId+"不在线，将消息'"+content+"'存入数据库置为未接收状态");
           build.setState(1);
       }
        messageService.save(build);
        return messageVo;
    }


    @ApiOperation("获取与某个用户的聊天记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chat_user_id",value = "聊天对象Id",required = true,paramType = "path",dataType = "Long"),
    })
    @GetMapping("/messages/{chat_user_id}")
    public List<MessageVo> userChatRecord(@PathVariable("chat_user_id") Long chat_user_id , HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        if (ObjectUtils.isEmpty(userService.getById(chat_user_id))||ObjectUtils.isEmpty(chat_user_id)){
            throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
        }
        List<MessageVo> messages= messageService.withUserChatRecord(user_id,chat_user_id);
        return messages;
    }


    @ApiOperation("获取与每个用户最后一条聊天记录以及未读消息数")
    @GetMapping("/messages")
    public List<ChatRecordVo> getAllChatUserLastRecord(HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        List<ChatRecordVo> messages= messageService.getAllChatUserLastRecord(user_id);
        return messages;
    }


    @ApiOperation("删除与某个用户的聊天记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chat_user_id",value = "聊天对象Id",required = true,paramType = "path",dataType = "Long"),
    })
    @DeleteMapping("/messages/{chat_user_id}")
    public void deleteMessage(@PathVariable("chat_user_id") Long chat_user_id , HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        if (ObjectUtils.isEmpty(userService.getById(chat_user_id))||ObjectUtils.isEmpty(chat_user_id)){
            throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
        }
        messageService.deleteChatRecord(user_id,chat_user_id);
    }


    @ApiOperation("获得总未读消息数")
    @GetMapping("/unRecieveCount")
    public Integer getunRecieveCount(HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        Integer integer = messageService.notReviceMessageCount(user_id);
        log.info("用户"+user_id+"未读信息"+integer+"条");
        return integer;
    }

    @GetMapping("/test/{user_id}")
    public List<ChatRecordVo> getAllChatUserLastRecord(@PathVariable("user_id") Long user_id){
        List<ChatRecordVo> messages= messageService.getAllChatUserLastRecord(user_id);
        System.out.println(messages);
        return messages;
    }





}
