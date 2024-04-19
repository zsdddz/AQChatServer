package com.howcode.darkchat.config;

import ch.qos.logback.core.PropertyDefinerBase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: ZhangWeinan
 * @Description: 适配后续的多节点部署日志唯一性
 * @date 2024-04-19 19:37
 */
public class IpLogConversionRule extends PropertyDefinerBase {
    @Override
    public String getPropertyValue() {
        return this.getLogIndex();
    }

    private String getLogIndex() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000));
    }
}
