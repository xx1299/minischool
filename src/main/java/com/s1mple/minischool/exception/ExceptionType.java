package com.s1mple.minischool.exception;

/**
 * 规定异常类型
 */
public enum ExceptionType {

    User_INPUT_ERROR(400,"您输入的数据错误，或者您的权限不足!"),
    SYSTEM_ERROR(500,"系统出现错误，请联系管理员或稍后再试!"),
    OTHER_ERROR(999,"出现未知错误，请联系管理员！");

    private int code;
    private String desc;

    ExceptionType(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

}
