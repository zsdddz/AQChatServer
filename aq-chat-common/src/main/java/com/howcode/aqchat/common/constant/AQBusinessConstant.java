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

    /**
     * 艾特
     */
    String AT = "@";
    /**
     * 机器人助手Id
     */
    String AI_HELPER_ID = "AQChatHelper";

    /**
     * 机器人助手名称
     */
    String AI_HELPER_NAME = "小Q";
    /**
     * 机器人助手头像
     */
    String AI_HELPER_AVATAR = "https://aqchat.oss-cn-shenzhen.aliyuncs.com/avatar/AQChatAI.png";

    /**
     * 小T
     */
    String XT_ID = "xt";
    /**
     * 小T名称
     */
    String XT_NAME = "小T";
    /**
     * 小T头像
     */
    String XT_AVATAR = "https://aqchat.oss-cn-shenzhen.aliyuncs.com/avatar/xt.png";

    /**
     * 小V
     */
    String XV_ID = "xv";
    /**
     * 小V名称
     */
    String XV_NAME = "小V";
    /**
     * 小V头像
     */
    String XV_AVATAR = "https://aqchat.oss-cn-shenzhen.aliyuncs.com/avatar/xv.png";

    /**
     * 小M
     */
    String XM_ID = "xm";
    /**
     * 小M名称
     */
    String XM_NAME = "小M";
    /**
     * 小M头像
     */
    String XM_AVATAR = "https://aqchat.oss-cn-shenzhen.aliyuncs.com/avatar/xm.png";
}
