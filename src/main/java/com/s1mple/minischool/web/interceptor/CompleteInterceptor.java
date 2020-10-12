package com.s1mple.minischool.web.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.s1mple.minischool.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CompleteInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        DecodedJWT claims = JwtUtils.getClaims(token);
        request.setAttribute("user_id",claims.getClaim("userId").asLong());
        request.setAttribute("openid",claims.getClaim("openid").asString());
        request.setAttribute("session_key",claims.getClaim("sessionKey").asString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
