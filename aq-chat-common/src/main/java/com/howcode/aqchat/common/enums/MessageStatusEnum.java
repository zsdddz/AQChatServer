package com.howcode.aqchat.common.enums;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-26 18:07
 */
public enum MessageStatusEnum implements AQChatEnum {
    HIDE(0, "hide"),
    SHOW(1, "show"),
    ;


    private final int code;
    private final String message;

    MessageStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
