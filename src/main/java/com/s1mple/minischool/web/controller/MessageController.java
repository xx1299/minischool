package com.s1mple.minischool.web.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1mple.minischool.domain.Vo.MessageVo;
import com.s1mple.minischool.domain.po.Message;
import com.s1mple.minischool.domain.po.User;
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

@Controller
@Slf4j
public class MessageController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @RequestMapping("/sendOne")
    @ResponseBody
    public Message sendMessage(@RequestParam("reciveId") Long reciveId, @RequestParam("message") String message, HttpServletRequest request) throws IOException, EncodeException {
//        Long user_id = (Long)request.getAttribute("user_id");
//        if (user_id.equals(reciveId)){
//            throw new CustomException(ExceptionType.OTHER_ERROR,"不能向自己发送信息");
//        }
//        Message build = Message.builder().content(message).receive_id(reciveId).send_id(user_id).sendTime(new Date()).build();
       if (ObjectUtils.isEmpty(userService.getById(reciveId))||ObjectUtils.isEmpty(reciveId)){
           throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
       }
       Message build = Message.builder().content(message).receive_id(reciveId).send_id(1310429363715567618L).sendTime(new Date()).state(0).build();

       if (!WebSocketServer.SendOne(reciveId,build)){
           log.info("用户"+reciveId+"不在线，将消息'"+message+"'存入数据库置为未接收状态");
           build.setState(1);;
       }
        messageService.save(build);
        return build;
    }

    @GetMapping("/messages/{reciveId}")
    public List<MessageVo> notReviceMessage(@PathVariable("reciveId") Long reciveId , HttpServletRequest request){
        Long user_id = (Long)request.getAttribute("user_id");
        if (ObjectUtils.isEmpty(userService.getById(reciveId))||ObjectUtils.isEmpty(reciveId)){
            throw new CustomException(ExceptionType.OTHER_ERROR,"未找到指定用户");
        }
        List<MessageVo> messages= messageService.notReviceMessage(user_id);
        return messages;
    }

    @RequestMapping("login")
    public String login(@RequestParam("user_id") Long user_id, Model model){

        System.out.println(user_id);
        model.addAttribute("userName","xx");
        model.addAttribute("userId",user_id);
        return "index";

    }

}
