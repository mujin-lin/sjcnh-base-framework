package com.sjcnh.apiclient.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenglin.wu
 * @description:
 * @title: RestTemplateConfig
 * @projectName ApiClient
 * @date 2021/8/13
 * @company WHY
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate createRestTemplate(){
        return new RestTemplateBuilder().build();
    }
}
