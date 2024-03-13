package com.sjcnh.commons;

/**
 * @author w
 * @description:
 * @title: RequestInstance
 * @projectName sjcnh-common
 * @date 2023/11/10
 * @company sjcnh-ctu
 */
public class RequestInstance<T> {
    private Class<?> requestClass;

    private T request;

    public RequestInstance(Class<?> requestClass) {
        this.requestClass = requestClass;
    }

    public RequestInstance(Class<?> requestClass, T request) {
        this.requestClass = requestClass;
        this.request = request;
    }

    public Class<?> getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(Class<T> requestClass) {
        this.requestClass = requestClass;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }
}
