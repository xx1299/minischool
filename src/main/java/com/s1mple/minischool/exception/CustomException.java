package com.s1mple.minischool.exception;

/**
 * 通用异常类
 */
public class CustomException extends RuntimeException {

    private int code;
    private String message;

    public CustomException(){

    }

    public CustomException(ExceptionType exceptionType){
        this.code = exceptionType.getCode();
        this.message = exceptionType.getDesc();
    }

    public CustomException(ExceptionType exceptionType,String message){
        this.code = exceptionType.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
