package com.howcode.aqchat.handler.at;

import com.howcode.aqchat.common.model.MessageDto;

public abstract class Handler {
    protected Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public abstract void handleMessage(MessageDto messageDto);
}