package com.howcode.aqchat.constant;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-20 16:26
 */
public class AQChatConstant {

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

    public interface AQBusinessPrefix{
        /**
         * 用户Id前缀
         */
        String AQ_USER_PREFIX = "aq";

        /**
         * 心跳时间
         */
        String HEART_BEAT_TIME = "heartBeatTime";
    }

    public interface AQRedisKeyPrefix {
        String AQ_USER_PREFIX = "aqchat:userId:";
    }
}
