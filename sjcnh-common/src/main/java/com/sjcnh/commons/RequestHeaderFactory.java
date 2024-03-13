package com.sjcnh.commons;


import com.sjcnh.commons.constants.ReflectConstants;
import com.sjcnh.commons.handler.HttpServletRequestHandler;
import com.sjcnh.commons.handler.RequestDealHandler;
import com.sjcnh.commons.handler.ServerRequestHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author w
 * @description:
 * @title: RequestHeaderFactory
 * @projectName sjcnh-common
 * @date 2023/11/10
 * @company sjcnh-ctu
 */
@SuppressWarnings("ALL")
public class RequestHeaderFactory {

    private static final Logger log = LoggerFactory.getLogger(RequestHeaderFactory.class);
    /**
     * handler map
     */
    private static final Map<String, RequestDealHandler> HANDLER_MAP = new HashMap<>();

    static {
        HANDLER_MAP.put(ReflectConstants.HTTP_SERVLET_REQUEST_PACKAGE, new HttpServletRequestHandler());

        HANDLER_MAP.put(ReflectConstants.REACTIVE_REQUEST_PACKAGE, new ServerRequestHandler());
    }

    /**
     * 注册处理对象
     *
     * @param dealClassName the dealClassName
     * @param handler       the handler
     * @return void
     * @author W
     * @date: 2023/11/10
     */
    public static void registerHandler(String dealClassName, RequestDealHandler handler) {
        HANDLER_MAP.put(dealClassName, handler);
    }

    /**
     * 获取指定header
     *
     * @param request       the request
     * @param specifyHeader the specifyHeader
     * @return String
     * @author W
     * @date: 2023/11/10
     */
    public static <T> String getSpecifyHeader(T request, String specifyHeader) {
        Class<?> aClass = request.getClass();
        RequestInstance<T> tRequestInstance = new RequestInstance<>(aClass, request);
        RequestDealHandler requestDealHandler = getHandler(aClass,request);
        return requestDealHandler.getSpecifyHeader(tRequestInstance, specifyHeader);
    }

    /**
     * 获取所有的header
     *
     * @param request the request
     * @return MultiValueMap<String, String>
     * @author W
     * @date: 2023/11/10
     */
    public static <T> MultiValueMap<String, String> getAllHeaders(T request) {
        Class<?> aClass = request.getClass();
        RequestDealHandler requestDealHandler = getHandler(aClass,request);
        RequestInstance<T> tRequestInstance = new RequestInstance<>(aClass, request);

        return requestDealHandler.getAllHeaders(tRequestInstance);
    }

    /**
     * 获取handler
     *
     * @param aClass the aClass
     * @return RequestDealHandler
     * @author W
     * @date: 2023/11/10
     */
    private static <T> RequestDealHandler getHandler(Class<?> aClass,T request) {
        String className = aClass.getName();
        String packageCanonicalName = ClassUtils.getPackageCanonicalName(aClass);
        StringBuilder packageNames = new StringBuilder();
        packageNames.append(packageCanonicalName);
        boolean proxyClass = Proxy.isProxyClass(aClass);
        if (proxyClass){
            try {
                conllectSuperClass(aClass,packageNames);
                conllectInterfaces(aClass,null,packageNames);
            } catch (Exception e) {
                log.error("had error:",e);
            }
        }
        boolean contains = packageNames.toString().contains(ReflectConstants.HTTP_SERVLET_REQUEST_PACKAGE);
        if (contains) {
            return HANDLER_MAP.get(ReflectConstants.HTTP_SERVLET_REQUEST_PACKAGE);
        }
        return HANDLER_MAP.get(ReflectConstants.REACTIVE_REQUEST_PACKAGE);
    }

    private static void conllectSuperClass(Class<?> aClass,StringBuilder packageNames){
        Class<?> superclass = aClass.getSuperclass();
//        Class<?>[] interfaces = aClass.getInterfaces();
        if (ObjectUtils.anyNull(superclass) ){
            return;
        }
        Package aPackage = superclass.getPackage();
        if (ObjectUtils.allNotNull(aPackage)){
            packageNames.append(aPackage.getName());
        }
        conllectSuperClass(superclass,packageNames);
    }
    private static void conllectInterfaces(Class<?> aClass,Class<?>[] interfaces,StringBuilder packageNames){
//        Class<?> superclass = aClass.getSuperclass();
        Class<?>[] interfacesNew = aClass.getInterfaces();
        if (ArrayUtils.isEmpty(interfacesNew) ){
            return;
        }
        for (Class<?> aClass1 : interfacesNew) {
            Package aPackage = aClass1.getPackage();
            if (ObjectUtils.allNotNull(aPackage)){
                packageNames.append(aPackage.getName());
            }
            Class<?>[] interfaces1 = aClass.getInterfaces();
            conllectInterfaces(aClass1,interfaces1,packageNames);
        }
    }

    private String getTargetName(final Object target) {
        if (target == null) {
            return "";
        }
        if (targetClassIsProxied(target)) {
            Advised advised = (Advised) target;
            try {
                return advised.getTargetSource().getTarget().getClass().getCanonicalName();
            } catch (Exception e) {
                return "";
            }
        }
        return target.getClass().getCanonicalName();
    }

    private boolean targetClassIsProxied(final Object target) {
        return target.getClass().getCanonicalName().contains("$Proxy");
    }

}
