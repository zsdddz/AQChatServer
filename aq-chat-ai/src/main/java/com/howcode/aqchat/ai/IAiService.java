package com.howcode.aqchat.ai;

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
}
