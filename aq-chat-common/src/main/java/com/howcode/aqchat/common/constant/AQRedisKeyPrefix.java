package com.howcode.aqchat.common.constant;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 17:42
 */
public interface AQRedisKeyPrefix {

    /**
     * 系统名称
     */
    String AQ_SYSTEM_NAME = "AQChat";

    /**
     * 用户登录信息前缀
     */
    String AQ_USER_INFO_PREFIX = AQ_SYSTEM_NAME + ":userInfo:";

    /**
     * 房间号缓存前缀
     */
    String AQ_ROOM_NO_PREFIX = AQ_SYSTEM_NAME + ":room:roomNo:";
    /**
     * 房间缓存前缀
     */
    String AQ_ROOM_PREFIX = AQ_SYSTEM_NAME + ":room:room:";
    /**
     * 房间信息缓存前缀
     */
    String AQ_ROOM_INFO_PREFIX = "info";
    /**
     * 房间成员缓存前缀
     */
    String AQ_ROOM_MEMBER_PREFIX = "member";
    /**
     * 阿里云临时凭证缓存
     */
    String ALI_OSS_STS = AQ_SYSTEM_NAME + ":aliOssSts";

    /**
     * 消息id缓存
     */
    String AQ_MESSAGE_ID = AQ_SYSTEM_NAME + ":messageId";
}
