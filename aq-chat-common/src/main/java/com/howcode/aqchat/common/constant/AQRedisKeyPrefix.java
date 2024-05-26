package com.howcode.aqchat.common.constant;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 17:42
 */
public interface AQRedisKeyPrefix {

    String SYS_NAME = "AQChat";
    /**
     * 用户登录信息前缀
     */
    String AQ_USER_INFO_PREFIX = SYS_NAME + ":userInfo:";

    /**
     * 房间号缓存前缀
     */
    String AQ_ROOM_NO_PREFIX = SYS_NAME + ":room:roomNo:";
    /**
     * 房间缓存前缀
     */
    String AQ_ROOM_PREFIX = SYS_NAME + ":room:room:";
    /**
     * 房间信息缓存前缀
     */
    String AQ_ROOM_INFO_PREFIX = "info";
    /**
     * 房间成员缓存前缀
     */
    String AQ_ROOM_MEMBER_PREFIX = "member";
    /**
     * 房间消息缓存前缀
     */
    String AQ_ROOM_MESSAGE_PREFIX = "message";
    /**
     * 阿里云临时凭证缓存
     */
    String ALI_OSS_STS = SYS_NAME + ":aliOssSts";
}
