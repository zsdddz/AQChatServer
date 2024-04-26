package com.howcode.aqchat.common.constant;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 17:42
 */
public interface AQRedisKeyPrefix {
    /**
     * 用户登录信息前缀
     */
    String AQ_USER_LOGIN_INFO_PREFIX = "AQChat:userLoginInfo:";

    /**
     * 房间号缓存前缀
     */
    String AQ_ROOM_NO_PREFIX = "AQChat:room:roomNo:";
    /**
     * 房间缓存前缀
     */
    String AQ_ROOM_PREFIX = "AQChat:room:room:";
    /**
     * 阿里云临时凭证缓存
     */
    String ALI_OSS_STS = "AQChat:aliOssSts";
}
