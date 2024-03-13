package com.sjcnh.abstraction.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: BaseFeignRequestInterceptor
 * @projectName sjcnh-base-framework
 * @date 2024/1/4
 * @company sjcnh-ctu
 */

public class BaseFeignRequestInterceptor implements RequestInterceptor {
    private final Logger log = LoggerFactory.getLogger(BaseFeignRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        this.execution(requestTemplate);
    }

    /**
     * 执行拦截的方法
     *
     * @param requestTemplate the requestTemplate
     * @return HttpServletRequest
     * @author W
     * @date: 2024/1/4
     */
    protected HttpServletRequest execution(RequestTemplate requestTemplate) {
        // 1. obtain request
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            log.error("MyFeignRequestInterceptor is invalid!");
            return null;
        }
        // 2. 兼容hystrix限流后，获取不到ServletRequestAttributes的问题（使拦截器直接失效）
        HttpServletRequest request = attributes.getRequest();
        // 2. obtain request headers，and put it into openFeign RequestTemplate
        Enumeration<String> headerNames = request.getHeaderNames();
        if (Objects.nonNull(headerNames)) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                //解决文件无法上传问题
                if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
                    continue;
                }
                requestTemplate.header(name, value);
            }
        }
        return request;
    }
}
