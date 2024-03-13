package com.sjcnh.ftp;


import com.sjcnh.ftp.client.FtpServerClient;

import java.io.IOException;

/**
 * @author chenglin.wu
 * @description:
 * @title: CreateClientListener
 * @projectName why-ftp
 * @date 2023/5/31
 * @company sjcnh-ctu
 */
public interface CreateClientListener {
    /**
     * 创建的IO异常
     *
     * @return FtpServerClient
     * @throws IOException 创建的IO异常
     * @author W
     * @date: 2023/5/31
     */
    FtpServerClient createClient() throws IOException;
}
