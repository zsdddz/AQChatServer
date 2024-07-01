package com.howcode.aqchat.common.enums;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 15:20
 */
public enum RoomType {
    NORMAL(1, "普通房间"),
    AI(2, "AI房间");

    private final int code;
    private final String desc;

    RoomType(int code, String desc) {
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
