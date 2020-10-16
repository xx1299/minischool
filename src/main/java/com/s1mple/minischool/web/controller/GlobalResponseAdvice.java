package com.s1mple.minischool.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1mple.minischool.domain.AjxsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 在返回数据前处理数据
 * 如果返回数据类型为json数据类型，使http状态码与封装对象状态码一致，未封装的类型封装为ajxs通用返回类
 * 其他类型直接返回
 */

@RestControllerAdvice(basePackages = "com.s1mple.minischool.web.controller")
@Component
@Slf4j
public class GlobalResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //如果响应结果是JSON数据类型
        if(mediaType.equalsTypeAndSubtype(
                MediaType.APPLICATION_JSON)){
            if(body instanceof AjxsResponse){
                AjxsResponse ajaxResponse = (AjxsResponse)body;
                if(ajaxResponse.getCode() != 999){ //999 不是标准的HTTP状态码，特殊处理
                    serverHttpResponse.setStatusCode(HttpStatus.valueOf(
                            ajaxResponse.getCode()
                    ));
                }else{
                    //999错误处理,状态码设置为500
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return body;
            }else if (body instanceof String){ //响应结果为String类型单单独处理
                ObjectMapper objectMapper = new ObjectMapper();
                serverHttpResponse.setStatusCode(HttpStatus.OK);
                AjxsResponse success = AjxsResponse.success(body);
                try {
                    body = objectMapper.writeValueAsString(success);
                } catch (JsonProcessingException e) {
                    log.error("对象转换字符串出错，错误信息："+e.getMessage());
                }
                return body;
            }else{
                serverHttpResponse.setStatusCode(HttpStatus.OK);
                return AjxsResponse.success(body);
            }

        }
        return body;
    }
}
