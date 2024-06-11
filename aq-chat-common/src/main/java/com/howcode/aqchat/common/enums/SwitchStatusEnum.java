package com.howcode.aqchat.common.enums;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-11 8:26
 */
public enum SwitchStatusEnum implements AQChatEnum{

    OPEN(1, "开启"),
    CLOSE(0, "关闭");

    private final Integer code;
    private final String desc;

    SwitchStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return this.desc;
    }
}
