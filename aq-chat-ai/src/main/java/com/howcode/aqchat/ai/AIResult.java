package com.howcode.aqchat.ai;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 17:52
 */
public interface AIResult {

    /**
     * Get content
     * @return content
     */
    String getContent();

    /**
     * Get status
     * @return status 0-开始 1-结束
     */
    int getStatus();
}
