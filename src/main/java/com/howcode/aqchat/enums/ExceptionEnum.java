package com.howcode.aqchat.enums;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-21 23:40
 */
public enum ExceptionEnum {
    UNKNOW_ERROR(10000, "未知错误"),
    USER_NOT_LOGIN(10001, "用户未登录"),
    ;

    private int code;
    private String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
