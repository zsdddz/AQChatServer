package com.howcode.aqchat.constant;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 16:26
 */
public class DarkChatConstant {

    public interface MessageHandlerConstant {
        /**
         * 指令处理器包名
         */
        String HANDLER_IMPLEMENTATION_PACKAGE_NAME = ".impl";
        /**
         * 指令处理器类名后缀
         */
        String HANDLER_CLASS_NAME_SUFFIX = "handler";
        /**
         * 指令处理器方法名
         */
        String HANDLER_METHOD_NAME = "handle";
    }
}