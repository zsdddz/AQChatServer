package com.howcode;

import com.howcode.handler.MessageHandler;
import com.howcode.session.Message;

import java.util.List;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:23
 */
public interface GiteeAIClient {
    //单轮对话
    String chat(String message);

    //多轮对话
    String chat(String message, List<Message> messages);

    //流式对话
    void streamChat(String message, List<Message> messages, MessageHandler<String> handler);

    //文字转图片
    byte[] textToImage(String message);

    //文字转语音
    byte[] textToVoice(String message);

}
