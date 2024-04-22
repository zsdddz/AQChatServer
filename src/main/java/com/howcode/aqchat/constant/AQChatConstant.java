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

    public interface AQBusinessConstant{
        /**
         * 用户Id前缀
         */
        String AQ_USER_PREFIX = "aq";

        /**
         * 心跳时间
         */
        String HEART_BEAT_TIME = "heartBeatTime";

        /**
         * userId
         */
        String USER_ID = "userId";
        /**
         * 服务器回复客户端心跳标志
         */
        String HEART_BEAT_ACK = "AQChat-Ack";

        /**
         * 用户信息缓存时长 单位秒 24小时
         */
        long USER_INFO_CACHE_TIME = 60 * 60 * 24;
    }

    public interface AQRedisKeyPrefix {
        /**
         * 用户登录信息前缀
         */
        String AQ_USER_LOGIN_INFO_PREFIX = "AQChat:userLoginInfo:";
    }
}
