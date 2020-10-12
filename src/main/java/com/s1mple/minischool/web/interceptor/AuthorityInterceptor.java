package com.s1mple.minischool.web.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1mple.minischool.domain.AjxsResponse;
import com.s1mple.minischool.domain.po.User;
import com.s1mple.minischool.exception.CustomException;
import com.s1mple.minischool.exception.ExceptionType;
import com.s1mple.minischool.service.UserService;
import com.s1mple.minischool.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)){
            returnJson(response,"尚未登陆");
            return false;
        }
        if (!JwtUtils.verifyToken(token)){
            returnJson(response,"token过期，请重新登陆");
            return false;
        }
        DecodedJWT claims = JwtUtils.getClaims(token);
        Long userId = claims.getClaim("userId").asLong();
//        System.out.println("userId"+userId);
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUser_id,userId));
        if (StringUtils.isEmpty(user)){
            returnJson(response,"不存在此用户");
            return false;
        }
//        System.out.println(user);
        if (!user.getOpenid().equals(claims.getClaim("openid").asString())
                ||!user.getSession_key().equals(claims.getClaim("sessionKey").asString())){
            returnJson(response,"凭证不正确");
            return false;
        }
        if (!claims.getClaim("state").asBoolean()){
            returnJson(response,"未完善资料");
            return false;
        }
        request.setAttribute("user_id",userId);
        return true;
    }

    private void returnJson(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(AjxsResponse.error(new CustomException(ExceptionType.OTHER_ERROR, message))));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
