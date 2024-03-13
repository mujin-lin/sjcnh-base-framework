package com.sjcnh.ftp;


import com.sjcnh.ftp.client.FtpServerClient;

import java.io.IOException;

/**
 * @author chenglin.wu
 * @description:
 * @title: DisconnectionListener
 * @projectName why-ftp
 * @date 2023/5/30
 * @company sjcnh-ctu
 */
public interface DisconnectionListener {
    /**
     * client 调用disconnect时的监听
     *
     * @param client the client
     * @throws IOException 关闭时的IO异常
     * @author W
     * @date: 2023/5/30
     */
    void disconnection(FtpServerClient client) throws IOException;
}
