package com.sjcnh.ftp.config;


import com.sjcnh.ftp.factory.FtpClientFactory;
import com.sjcnh.ftp.pool.FtpSource;
import com.sjcnh.ftp.service.FtpClientService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutionException;

/**
 * @author chenglin.wu
 * @description:
 * @title: FtpInitializationAutoConfiguration
 * @projectName why-ftp
 * @date 2023/5/29
 * @company sjcnh-ctu
 */
@Configuration
@AutoConfigureAfter(FtpConfig.class)
public class FtpInitializationAutoConfiguration {

    @Bean
    @ConditionalOnBean(FtpConfig.class)
    public FtpClientFactory createFtpFactory(FtpConfig ftpConfig) {
        return new FtpClientFactory(ftpConfig);
    }

    @Bean
    @ConditionalOnBean(FtpClientFactory.class)
    public FtpSource createFtpSource(FtpClientFactory factory) throws ExecutionException, InterruptedException {
        return new FtpSource(factory);
    }

    @Bean
    @ConditionalOnBean(value = {FtpClientFactory.class,FtpConfig.class})
    public FtpClientService createFtpClientService(FtpClientFactory factory, FtpConfig ftpConfig){
        return new FtpClientService(factory,ftpConfig);
    }



}
