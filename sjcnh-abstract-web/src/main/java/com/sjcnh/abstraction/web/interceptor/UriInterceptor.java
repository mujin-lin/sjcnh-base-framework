package com.sjcnh.abstraction.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenglin.wu
 * @description:
 * @title: UriInterceptor
 * @projectName sjcnh-abstract-web
 * @date 2023/7/6
 * @company sjcnh-ctu
 */
@SuppressWarnings("all")
public class UriInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(UriInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("uri {}", request.getRequestURI());
        return Boolean.TRUE;
    }
}
