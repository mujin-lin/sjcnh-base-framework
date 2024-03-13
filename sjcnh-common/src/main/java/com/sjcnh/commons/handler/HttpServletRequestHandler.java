package com.sjcnh.commons.handler;


import com.google.common.collect.Lists;
import com.sjcnh.commons.RequestInstance;
import com.sjcnh.commons.constants.ReflectConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @author w
 * @description:
 * @title: HttpServletRequestHandler
 * @projectName sjcnh-common
 * @date 2023/11/10
 * @company sjcnh-ctu
 */
public class HttpServletRequestHandler implements RequestDealHandler {

    private static final Logger log = LoggerFactory.getLogger(HttpServletRequestHandler.class);



    @Override
    public <T> String getSpecifyHeader(RequestInstance<T> requestInstance, String specifyHeader) {
        Class<?> requestClass = requestInstance.getRequestClass();
        T request = requestInstance.getRequest();
        String header = "";
        Method method;
        try {
            method = requestClass.getMethod(ReflectConstants.HTTP_SERVLET_REQUEST_GET_HEADER, String.class);
            if (ObjectUtils.anyNull(method)){
                return header;
            }
            // 设置方法访问权限
            setMethodPublic(method);
            header = method.invoke(request, specifyHeader).toString();
        } catch (Exception e) {
            log.error("get header has error: ", e);
        }
        return header;
    }

    @Override
    public <T> MultiValueMap<String, String> getAllHeaders(RequestInstance<T> requestInstance) {
        Class<?> requestClass = requestInstance.getRequestClass();
        T request = requestInstance.getRequest();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        try {
            Method allHeaderNames = requestClass.getMethod(ReflectConstants.HTTP_SERVLET_REQUEST_GET_HEADER_NAMES);
            Method getHeader = requestClass.getMethod(ReflectConstants.HTTP_SERVLET_REQUEST_GET_HEADER, String.class);
            Enumeration<String> headerNames = (Enumeration<String>) allHeaderNames.invoke(request);
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerVal = (String) getHeader.invoke(request, headerName);
                headers.put(headerName, Lists.newArrayList(headerVal));
            }
        } catch (Exception e) {
            log.error("get all header has error: ", e);
        }
        return headers;
    }
}
