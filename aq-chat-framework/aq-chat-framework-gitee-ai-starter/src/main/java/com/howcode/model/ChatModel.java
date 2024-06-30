package com.howcode.model;

import com.howcode.handler.MessageHandler;
import com.howcode.session.Message;

import java.util.List;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:54
 */
public interface ChatModel {
    //单轮对话
    String chat(String message);

    //多轮对话
    String chat(String message, List<Message> messages);

    //流式对话
    void streamChat(String message, List<Message> messages, MessageHandler<String> handler);
}
