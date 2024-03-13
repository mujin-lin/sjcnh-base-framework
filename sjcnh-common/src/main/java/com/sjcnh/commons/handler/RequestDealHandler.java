package com.sjcnh.commons.handler;


import com.sjcnh.commons.RequestInstance;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author w
 * @description:
 * @title: RequestHandler
 * @projectName sjcnh-common
 * @date 2023/11/10
 * @company sjcnh-ctu
 */
public interface RequestDealHandler {

    /**
     * 获取指定的header
     *
     * @param requestInstance the request
     * @param specifyHeader   the specifyHeader
     * @return String
     * @author W
     * @date: 2023/11/10
     */
    <T> String getSpecifyHeader(RequestInstance<T> requestInstance, String specifyHeader);

    /**
     * 获取当前请求的所有header
     *
     * @param requestInstance the request
     * @return MultiValueMap<String, String>
     * @author W
     * @date: 2023/11/10
     */
    <T> MultiValueMap<String, String> getAllHeaders(RequestInstance<T> requestInstance);
    /**
     * 设置方法的访问权限
     * @param method the method
     * @author W
     * @date: 2023/11/21
     */
    default void setMethodPublic(Method method){
        if (ObjectUtils.anyNull(method)){
            return;
        }
        int modifiers = method.getModifiers();
        boolean isPublic = Modifier.isPublic(modifiers);
        if (!isPublic){
            method.setAccessible(Boolean.TRUE);
        }
    }

}
