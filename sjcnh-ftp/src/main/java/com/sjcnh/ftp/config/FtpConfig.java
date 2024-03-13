package com.sjcnh.ftp.config;


import com.sjcnh.ftp.CustomOnPropertyCondition;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author chenglin.wu
 * @description:
 * @title: FtpConfig
 * @projectName why-ftp
 * @date 2023/5/12
 * @company sjcnh-ctu
 */
@Configuration
@SuppressWarnings("unused")
@ConfigurationProperties("file.server.ftp")
@Conditional(CustomOnPropertyCondition.class)
public class FtpConfig extends FTPClientConfig {

    /**
     * 账号
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * ftp服务器地址
     */
    private String host;
    /**
     * 端口
     */
    private int port = 21;
    /**
     * 连接池初始化数量
     */
    private int initSize = 5;
    /**
     * 连接池最大数量
     */
    private int maxSize = 10;
    /**
     * 检测时间间隔（毫秒数）
     */
    private int checkInterval = 10000;
    /**
     * 编码格式
     */
    private Charset controlEncoding = StandardCharsets.UTF_8;
    /**
     * 保存实际上传的文件名,如果为false则生成uuid的文件名
     */
    private boolean keepOriginalFilename;
    /**
     * client 缓冲区大小
     */
    private int clientBufferSize = 1024;
    /**
     * 是否使用utf8解码
     */
    private boolean autodetectUtf8 =true;
    /**
     * 中文路径和文件名编码
     */
    private Charset chinesePathOrFileName;

    private Charset charset = StandardCharsets.UTF_8;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getInitSize() {
        return initSize;
    }

    public void setInitSize(int initSize) {
        this.initSize = initSize;
    }


    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }


    public boolean isKeepOriginalFilename() {
        return keepOriginalFilename;
    }

    public void setKeepOriginalFilename(boolean keepOriginalFilename) {
        this.keepOriginalFilename = keepOriginalFilename;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getClientBufferSize() {
        return clientBufferSize;
    }

    public void setClientBufferSize(int clientBufferSize) {
        this.clientBufferSize = clientBufferSize;
    }


    public boolean isAutodetectUtf8() {
        return autodetectUtf8;
    }

    public void setAutodetectUtf8(boolean autodetectUtf8) {
        this.autodetectUtf8 = autodetectUtf8;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(Charset controlEncoding) {
        this.controlEncoding = controlEncoding;
    }
}
