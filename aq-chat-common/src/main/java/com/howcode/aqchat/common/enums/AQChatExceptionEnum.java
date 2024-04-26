package com.howcode.aqchat.common.enums;

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
    GET_STS_FAILED(10004, "获取阿里云临时凭证失败"),
    //消息类型不存在
    MSG_TYPE_NOT_EXIST(10005, "消息类型不存在"),
    //用户不存在或者退出
    USER_DOES_NOT_EXIST_OR_EXITS(10006, "用户不存在或者退出"),
    //用户id不匹配
    USER_ID_NOT_MATCH(10007, "用户id不匹配"),
    ;

    private final int code;
    private final String message;

    AQChatExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
