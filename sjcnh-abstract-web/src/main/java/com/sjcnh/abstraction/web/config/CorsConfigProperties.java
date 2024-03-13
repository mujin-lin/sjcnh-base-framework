package com.sjcnh.abstraction.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author w
 * @description:
 * @title: CorsConfigPreproties
 * @projectName sjcnh-base-framework
 * @date 2023/12/6
 * @company sjcnh-ctu
 */
@ConfigurationProperties("sjcnh.web.global.config.cors")
public class CorsConfigProperties {
    /**
     * 可能会跨域的路径
     */
    private String mappingPathPattern = "/**";
    /**
     * 允许跨域的匹配
     */
    private String[] allowedOriginPatterns = {"*"};
    /**
     * 允许跨域的header
     */
    private String[] allowedHeaders = {"requestsource","RequestSource","Referer","sec-ch-ua","sec-ch-ua-mobile","Sec-Fetch-Dest",
            "Sec-Fetch-Mode","Sec-Fetch-Site","User-Agent","Host","Authorization","Origin","X-Requested-With",
            "ContentType","Content-Type","Accept","Accept-Encoding","Accept-Language"};
    /**
     * 允许跨域的请求方法
     */
    private String[] allowedMethods = {"GET", "POST", "DELETE", "PUT", "OPTIONS"};

    /**
     * 最大多少时间内不用发送option请求
     */
    private long maxAge = 1800000L;
    /**
     * 允许携带cookie等信息
     */
    private boolean allowCredentials = true;

    public String getMappingPathPattern() {
        return mappingPathPattern;
    }

    public void setMappingPathPattern(String mappingPathPattern) {
        this.mappingPathPattern = mappingPathPattern;
    }

    public String[] getAllowedOriginPatterns() {
        return allowedOriginPatterns;
    }

    public void setAllowedOriginPatterns(String[] allowedOriginPatterns) {
        this.allowedOriginPatterns = allowedOriginPatterns;
    }

    public String[] getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String[] allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public String[] getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(String[] allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }
}
