package com.howcode.aqchat.common.enums;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/7/2 10:53
 */
public enum AISpaceStatusEnum implements AQChatEnum{
    //0 空闲 1 工作中
    IDLE(0, "空闲"),
    WORKING(1, "工作中");

    private final int code;
    private final String message;

    AISpaceStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
