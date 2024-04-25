package com.howcode.aqchat.model;

import lombok.Data;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 12:29
 */
@Data
public class AliOssStsDto {
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String region;
    private String bucket;
    private String uploadPath;
    private String ossEndpoint;
}
