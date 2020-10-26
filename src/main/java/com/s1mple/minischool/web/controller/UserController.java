package com.s1mple.minischool.web.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1mple.minischool.domain.AjxsResponse;
import com.s1mple.minischool.domain.Vo.UserVo;
import com.s1mple.minischool.domain.User;
import com.s1mple.minischool.service.UserService;
import com.s1mple.minischool.utils.Base64Utils;
import com.s1mple.minischool.utils.DozerUtils;
import com.s1mple.minischool.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Date;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户模块", value = "登录，完善资料，验证token")
public class UserController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    private Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "微信code",required = true,paramType = "body",dataType = "String"),
    })
    @PostMapping("/login")
    public UserVo login(@RequestBody String code) throws JsonProcessingException, UnsupportedEncodingException {
        String forObject = restTemplate
                .getForObject("https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={SECRET}&js_code={JSCODE}&grant_type={authorization_code}"
                , String.class, "wx0516e7a54319bf70"
                , "c7bc808bbf6069ed3af4fbf83170cde2", code, "client_credential");
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(forObject, User.class);
        User getUser = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getOpenid,user.getOpenid()));
        if (StringUtils.isEmpty(getUser)){
            user.setState(false);
            userService.save(user);
        }else{
            user = getUser;
        }
        user.setLastLoginTime(new Date(System.currentTimeMillis()));
        userService.updateById(user);
        UserVo userVo = mapper.map(user,UserVo.class);
        userVo.setToken(JwtUtils.createToken(user));
//        if(!StringUtils.isEmpty(user.getNickName())){
//            userVo.setNickName(Base64Utils.decode(user.getNickName()));
//        }
        return userVo;
    }

    @ApiOperation("完善资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "用户对象",required = true,paramType = "body",dataType = "User"),
    })
    @PostMapping("/complete")
    public UserVo userComplete(@RequestBody User user,HttpServletRequest request) throws UnsupportedEncodingException, JsonProcessingException {
        user.setUser_id((Long)request.getAttribute("user_id"));
        user.setOpenid((String)request.getAttribute("openid"));
        user.setSession_key((String)request.getAttribute("session_key"));
//        user.setNickName(Base64Utils.encode(user.getNickName()));
        user.setState(true);
        userService.updateById(user);
        UserVo userVo = DozerUtils.map(user, UserVo.class);
        userVo.setToken(JwtUtils.createToken(user));
        return userVo;
    }

    @ApiOperation("验证token")
    @GetMapping("/check")
    public AjxsResponse checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)){
            return AjxsResponse.success("请登陆",false);
        }
        boolean bool = JwtUtils.verifyToken(token);
        return bool?AjxsResponse.success(true):AjxsResponse.success("token已过期，请重新登陆",false);
    }


}


