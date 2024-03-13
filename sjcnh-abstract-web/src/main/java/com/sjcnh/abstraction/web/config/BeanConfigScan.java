package com.sjcnh.abstraction.web.config;

import com.sjcnh.abstraction.redis.manager.RedisTransEncryptManager;
import com.sjcnh.abstraction.web.advice.CommonExceptionHandler;
import com.sjcnh.abstraction.web.condition.CorsCondition;
import com.sjcnh.abstraction.web.condition.GlobalControllerCondition;
import com.sjcnh.abstraction.web.controller.GlobalController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @author chenglin.wu
 * @description:
 * @title: BeanConfigScan
 * @projectName sjcnh-abstract-web
 * @date 2023/7/6
 */
@SuppressWarnings("ALL")
@ConfigurationProperties("sjcnh.web.global.config")
public class BeanConfigScan {
    /**
     * 是否开启跨域配置
     */
    private boolean enableCors;
    /**
     * 否开启全局controller获取动态加密数据
     */
    private boolean enableGlobalController;

    public boolean isEnableCors() {
        return enableCors;
    }

    public void setEnableCors(boolean enableCors) {
        this.enableCors = enableCors;
    }

    public boolean isEnableGlobalController() {
        return enableGlobalController;
    }

    public void setEnableGlobalController(boolean enableGlobalController) {
        this.enableGlobalController = enableGlobalController;
    }

//    @PostConstruct
//    public void registerBean() {
//        ApplicationContextUtil.registerBean("commonExceptionHandler", CommonExceptionHandler.class, Collections.EMPTY_LIST, null);
//
//    }

    @Bean
    @ConditionalOnMissingBean(CommonExceptionHandler.class)
    public CommonExceptionHandler createCommonExceptionHandler() {
        return new CommonExceptionHandler();
    }

    @Bean
    @Conditional(CorsCondition.class)
    @ConditionalOnBean(CorsConfigProperties.class)
    public CorsConfig createCorsConfig(CorsConfigProperties corsConfigProperties) {
        return new CorsConfig(corsConfigProperties);
    }

    @Bean
    @ConditionalOnBean(RedisTransEncryptManager.class)
    @Conditional(GlobalControllerCondition.class)
    public GlobalController createGlobalController(RedisTransEncryptManager redisTransEncryptManager) {
        return new GlobalController(redisTransEncryptManager);
    }

}
