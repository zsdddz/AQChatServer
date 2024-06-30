package com.howcode.handler;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 12:06
 */
public interface MessageHandler<T> {

    void handle(T message);
}
