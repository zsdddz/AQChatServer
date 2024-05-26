package com.howcode.aqchat.common.constant;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-26 17:41
 */
public interface AQBusinessConstant {
    /**
     * 用户Id前缀
     */
    String AQ_USER_PREFIX = "aquser_";
    /**
     * 房间Id前缀
     */
    String AQ_ROOM_PREFIX = "aqroom_";

    /**
     * 心跳时间
     */
    String HEART_BEAT_TIME = "heartBeatTime";

    /**
     * userId
     */
    String USER_ID = "userId";
    /**
     * roomId
     */
    String ROOM_ID = "roomId";
    /**
     * 服务器回复客户端心跳标志
     */
    String HEART_BEAT_ACK = "AQChat-Ack";

    /**
     * 用户信息缓存时长 单位秒 24小时
     */
    long USER_INFO_CACHE_TIME = 60 * 60 * 24;
    /**
     * 用户离线后缓存时间  9分钟
     */
    long USER_OFFLINE_CACHE_TIME = 60 * 9;
    /**
     * 阿里云临时凭证缓存时长 单位秒 1小时
     */
    long ALI_OSS_STS_CACHE_TIME = 60 * 60 - 60;
    /**
     * 上传路径 时间格式
     */
    String UPLOAD_PATH_DATE_FORMAT = "yyyy-MM-dd";

    String LIMIT = "limit 100";
}
