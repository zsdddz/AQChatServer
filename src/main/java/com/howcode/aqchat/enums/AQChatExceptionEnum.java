package com.howcode.aqchat.enums;

import lombok.Getter;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-21 23:40
 */
@Getter
public enum AQChatExceptionEnum implements AQChatEnum{
    UNKNOW_ERROR(10000, "未知错误"),
    USER_NOT_LOGIN(10001, "用户未登录"),
    //房间已存在
    ROOM_EXIST(10002, "房间已存在"),
    //房间不存在
    ROOM_NOT_EXIST(10003, "房间不存在"),
    ;

    private final int code;
    private final String message;

    AQChatExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}