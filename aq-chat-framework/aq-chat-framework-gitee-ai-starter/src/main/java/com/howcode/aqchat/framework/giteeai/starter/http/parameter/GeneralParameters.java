package com.howcode.aqchat.framework.giteeai.starter.http.parameter;



import com.howcode.aqchat.framework.giteeai.starter.session.Message;

import java.util.List;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/29 22:11
 */

public class GeneralParameters {
    private List<Message> messages;
    private String inputs;
    private Boolean stream;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }
}
