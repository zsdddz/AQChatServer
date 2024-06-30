package com.howcode.aqchat.ai;

import com.howcode.aqchat.ai.parameter.MessageRecord;

import java.util.List;
import java.util.function.Consumer;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 17:23
 */
public interface IAiService {
    /**
     * Get answer from AI
     * @param question question
     * @return answer
     */
    String getAnswer(String question);

    /**
     * Stream call with message
     * @param userMsg user message
     * @param consumer consumer
     */
    void streamCallWithMessage(String userMsg, Consumer<AIResult> consumer);

    //多轮对话
    default void chat(String message, List<MessageRecord> messages,Consumer<AIResult> consumer){
    }

    //文字转图片
    default AIResult textToImage(String text){
        return null;
    }

    //文字转语音
    default AIResult textToVoice(String text){
        return null;
    }

}
