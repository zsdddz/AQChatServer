package com.howcode.aqchat.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.howcode.aqchat.config.AQChatConfig;
import com.howcode.aqchat.constant.AQChatConstant;
import com.howcode.aqchat.model.AliOssStsDto;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-25 11:57
 */
@Component
public class AliOssProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliOssProvider.class);

    @Resource
    private AQChatConfig aqChatConfig;
    @Resource
    private RedisCacheHelper redisCacheHelper;

    /**
     * 获取阿里临时凭证
     */
    public AliOssStsDto getAliOssSts() {
        String endpoint = aqChatConfig.getAliOssStsConfig().getEndpoint();
        String accessKeyId = aqChatConfig.getAliOssStsConfig().getAccessKeyId();
        String accessKeySecret = aqChatConfig.getAliOssStsConfig().getAccessKeySecret();
        String roleArn = aqChatConfig.getAliOssStsConfig().getRoleArn();
        String roleSessionName = aqChatConfig.getAliOssStsConfig().getRoleSessionName();
        Long durationSeconds = aqChatConfig.getAliOssStsConfig().getDurationSeconds();
        AliOssStsDto aliOssStsDto = getCacheAliOssSts();
        if (aliOssStsDto != null) {
            return aliOssStsDto;
        }
        try {
            String regionId = "";
            DefaultProfile.addEndpoint(regionId, "Sts", endpoint);
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setDurationSeconds(durationSeconds);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            aliOssStsDto = new AliOssStsDto();
            aliOssStsDto.setAccessKeyId(response.getCredentials().getAccessKeyId());
            aliOssStsDto.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
            aliOssStsDto.setSecurityToken(response.getCredentials().getSecurityToken());
            aliOssStsDto.setBucket(aqChatConfig.getAliOssStsConfig().getBucketName());
            aliOssStsDto.setRegion(aqChatConfig.getAliOssStsConfig().getRegionId());
            aliOssStsDto.setOssEndpoint(aqChatConfig.getAliOssConfig().getEndpoint());
            cacheAliOssSts(aliOssStsDto);
        } catch (ClientException e) {
            LOGGER.error("Failed to get AliOssSts,Error message: {}", e.getErrMsg());
        }
        return aliOssStsDto;
    }

    /**
     * 缓存阿里临时凭证
     */
    private void cacheAliOssSts(AliOssStsDto aliOssStsDto) {
        if (aliOssStsDto != null) {
            redisCacheHelper.setCacheObject(AQChatConstant.AQRedisKeyPrefix.ALI_OSS_STS,
                    aliOssStsDto,
                    AQChatConstant.AQBusinessConstant.ALI_OSS_STS_CACHE_TIME,
                    TimeUnit.SECONDS);
        }
    }

    /**
     * 获取缓存的阿里临时凭证
     */
    private AliOssStsDto getCacheAliOssSts() {
        return redisCacheHelper.getCacheObject(AQChatConstant.AQRedisKeyPrefix.ALI_OSS_STS,AliOssStsDto.class);
    }
}
