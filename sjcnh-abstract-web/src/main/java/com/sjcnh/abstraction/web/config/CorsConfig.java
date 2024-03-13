package com.sjcnh.abstraction.web.config;

import com.sjcnh.abstraction.web.interceptor.UriInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author w
 * @description:
 * @title: CorsConfig
 * @projectName sjcnh-abstract-web
 * @date 2021年05月27日
 */
public class CorsConfig implements WebMvcConfigurer {
    
    private final CorsConfigProperties corsConfigProperties;

    public CorsConfig(CorsConfigProperties corsConfigProperties) {
        this.corsConfigProperties = corsConfigProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有方法
        registry.addMapping(this.corsConfigProperties.getMappingPathPattern())
                //允许的域名
                .allowedOriginPatterns(this.corsConfigProperties.getAllowedOriginPatterns())
                .allowCredentials(this.corsConfigProperties.isAllowCredentials())
                // 允许请求头
                .allowedHeaders(this.corsConfigProperties.getAllowedHeaders())
                //允许方法
                .allowedMethods(this.corsConfigProperties.getAllowedMethods())
                //表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(this.corsConfigProperties.getMaxAge());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UriInterceptor()).addPathPatterns("/**");
    }

}
