package com.yjfei.antibot.stream.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ons")
@Component
@Data
public class AliyunOnsProperties {
    private String accessKey;
    private String secretKey;
    private String nameServerAddress;
    private String dingTalkServerUrl;

    public AliyunOnsProperties() {
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public AliyunOnsProperties setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public AliyunOnsProperties setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getNameServerAddress() {
        return this.nameServerAddress;
    }

    public AliyunOnsProperties setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
        return this;
    }

    public String getDingTalkServerUrl() {
        return this.dingTalkServerUrl;
    }

    public AliyunOnsProperties setDingTalkServerUrl(String dingTalkServerUrl) {
        this.dingTalkServerUrl = dingTalkServerUrl;
        return this;
    }
}
