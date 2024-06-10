package com.howcode.aqchat.common.enums;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 20:56
 */
public enum AIMessageStatusEnum implements AQChatEnum{
    WAIT(0, "wait"),
    END(1, "end"),
    FAIL(2, "fail"),;

    private final int code;
    private final String message;

    AIMessageStatusEnum(int code, String message) {
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
