package com.howcode.aqchat.ai.model;

import com.howcode.aqchat.ai.AIResult;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 17:58
 */
public class QWResult implements AIResult {

    private String content;
    private int status;

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "QWResult{" +
                "content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
