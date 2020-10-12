package com.s1mple.minischool.domain;

import com.s1mple.minischool.exception.CustomException;
import lombok.Data;

@Data
public class AjxsResponse {

    private String message;

    private Integer code;

    private boolean isok;

    private Object data;

    public static AjxsResponse success(){
        AjxsResponse ajxsResponse = new AjxsResponse();
        ajxsResponse.setIsok(true);
        ajxsResponse.setCode(200);
        ajxsResponse.setMessage("请求成功");
        return ajxsResponse;
    }

    public static AjxsResponse success(Object data){
        AjxsResponse ajxsResponse = new AjxsResponse();
        ajxsResponse.setIsok(true);
        ajxsResponse.setCode(200);
        ajxsResponse.setMessage("请求成功");
        ajxsResponse.setData(data);
        return ajxsResponse;
    }

    public static AjxsResponse success(String message, Object data){
        AjxsResponse ajxsResponse = new AjxsResponse();
        ajxsResponse.setIsok(true);
        ajxsResponse.setCode(200);
        ajxsResponse.setMessage(message);
        ajxsResponse.setData(data);
        return ajxsResponse;
    }

    public static AjxsResponse error(CustomException exception){
        AjxsResponse ajxsResponse = new AjxsResponse();
        ajxsResponse.setIsok(false);
        ajxsResponse.setCode(exception.getCode());
        ajxsResponse.setMessage(exception.getMessage());
        return ajxsResponse;
    }

    public static AjxsResponse error(CustomException exception,String message){
        AjxsResponse ajxsResponse = new AjxsResponse();
        ajxsResponse.setIsok(false);
        ajxsResponse.setCode(exception.getCode());
        ajxsResponse.setMessage(message);
        return ajxsResponse;
    }

}
