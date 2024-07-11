package com.howcode.aqchat.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.howcode.aqchat.common.config.AQChatConfig;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 13:55
 */
@Component
public class ALIOSSHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ALIOSSHelper.class);

    @Resource
    private AQChatConfig aqChatConfig;
    //阿里oss上传文件
    public String upload(String fileName, byte[] bytes) {

        //通过工具类来获取相应的值
        String endpoint = aqChatConfig.getAliOssConfig().getEndpoint();
        String accessKeyId = aqChatConfig.getAliOssStsConfig().getAccessKeyId();
        String accessKeySecret = aqChatConfig.getAliOssStsConfig().getAccessKeySecret();
        String bucketName = aqChatConfig.getAliOssStsConfig().getBucketName();


        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream is = new ByteArrayInputStream(bytes);
            ossClient.putObject(bucketName, fileName, is);
            ossClient.shutdown();
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            LOGGER.info("上传文件成功，url:{}", url);
            return url;
        } catch (Exception e) {
            LOGGER.error("上传文件失败", e);
        }
        return "";
    }
}
