package com.howcode.aqchat.holder;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-05-24 10:32
 */
public interface IMessageHolder {

    //存储消息id
    void putMessageId(String roomId, String msgId);

    //判断消息id是否存在
    boolean isExistMessageId(String roomId, String msgId);
}
