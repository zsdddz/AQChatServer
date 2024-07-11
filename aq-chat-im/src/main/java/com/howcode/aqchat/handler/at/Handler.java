package com.howcode.aqchat.handler.at;

import com.howcode.aqchat.common.model.MessageDto;

public abstract class Handler {
    protected Handler handler;

    public void setHandler(Handler handler) {
        if (this.handler == null) {
            this.handler = handler;
        }else {
            this.handler.setHandler(handler);
        }
    }

    public abstract void handleMessage(MessageDto messageDto);
}