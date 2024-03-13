package com.sjcnh.ftp.constants;

/**
 * @author chenglin.wu
 * @description:
 * @title: FtpConstants
 * @projectName why-ftp
 * @date 2023/6/2
 * @company sjcnh-ctu
 */
public final class FtpConstants {

    private FtpConstants() {
    }

    /**
     * ftp 客户端的根目录
     */
    public static final String CLIENT_ROOT_DIR = "/";
    /**
     * 使用utf8编码时需要预发送命令
     */
    public static final String USE_UTF8_COMMAND = "OPTS UTF8";
    /**
     * 开启命令
     */
    public static final String USE_UTF8_COMMAND_ON = "ON";
}
