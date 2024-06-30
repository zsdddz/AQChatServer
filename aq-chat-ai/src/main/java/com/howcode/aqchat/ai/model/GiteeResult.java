package com.howcode.aqchat.ai.model;

import com.howcode.aqchat.ai.AIResult;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 14:02
 */
public class GiteeResult implements AIResult {
    private String content;
    private int status;
    private int type;

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GiteeResult{" +
                "content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
