package com.howcode.aqchat.common.enums;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 14:16
 */
public enum MsgTypeEnum implements AQChatEnum{
    IMAGE(1, "image"),
    VOICE(2, "voice"),
    VIDEO(3, "video"),
    ;

    private final int code;
    private final String message;

    MsgTypeEnum(int code, String message) {
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
    /**
     * 通过code 获取枚举名字
     */
    public static String getMsgTypeByCode(int code) {
        for (MsgTypeEnum value : MsgTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getMessage();
            }
        }
        return null;
    }
}
