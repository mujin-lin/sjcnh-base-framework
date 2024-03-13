package com.sjcnh.commons.handler;


import com.sjcnh.commons.RequestInstance;
import com.sjcnh.commons.constants.ReflectConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;

/**
 * @author w
 * @description:
 * @title: ServerRequestHandler
 * @projectName sjcnh-common
 * @date 2023/11/10
 * @company sjcnh-ctu
 */
@SuppressWarnings("all")
public class ServerRequestHandler implements RequestDealHandler {
    private static final Logger log = LoggerFactory.getLogger(ServerRequestHandler.class);

    @Override
    public <T> String getSpecifyHeader(RequestInstance<T> requestInstance, String specifyHeader) {
        T request = requestInstance.getRequest();
        Class<?> requestClass = requestInstance.getRequestClass();
        String header = "";
        try {

            Method method = requestClass.getMethod(ReflectConstants.SERVER_REQUEST_GET_HEADERS);
            if (ObjectUtils.anyNull(method)){
                return header;
            }
            // 设置方法访问权限
            setMethodPublic(method);
            MultiValueMap<String, String> httpHeaders = (MultiValueMap<String, String>) method.invoke(request);
            header = httpHeaders.getFirst(specifyHeader);
        } catch (Exception e) {
            log.error("get header has error: ", e);
        }
        return header;
    }

    @Override
    public <T> MultiValueMap<String, String> getAllHeaders(RequestInstance<T> requestInstance) {
        T request = requestInstance.getRequest();
        Class<?> requestClass = requestInstance.getRequestClass();
        MultiValueMap<String, String> httpHeaders = new LinkedMultiValueMap<>();
        try {
            Method method = requestClass.getMethod(ReflectConstants.SERVER_REQUEST_GET_HEADERS);
            if (ObjectUtils.anyNull(method)){
                return httpHeaders;
            }
            // 设置方法访问权限
            setMethodPublic(method);
            httpHeaders = (MultiValueMap<String, String>)method.invoke(request);
        } catch (Exception e) {
            log.error("get header has error: ", e);
        }
        return httpHeaders;
    }
}
