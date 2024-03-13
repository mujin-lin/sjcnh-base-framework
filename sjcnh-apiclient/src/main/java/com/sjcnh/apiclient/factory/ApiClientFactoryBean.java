package com.sjcnh.apiclient.factory;

import com.sjcnh.apiclient.poxy.ApiClientInvocationHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cglib.proxy.Proxy;

/**
 * @author chenglin.wu
 * @description:
 * @title: ApiClientFactoryBean
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
public class ApiClientFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    /**
     * 代理的接口
     */
    private Class<T> apiClientInterface;

    /**
     * 代理对象
     */
    private T apiClientObject;

    public ApiClientFactoryBean(Class<T> mapperInterface) {
        if(mapperInterface == null || !mapperInterface.isInterface()) {
            throw new IllegalArgumentException(mapperInterface + " must be a interface.");
        }
        this.apiClientInterface = mapperInterface;
    }

    @SuppressWarnings("unchecked")
    private T createProxy() {
        return (T) Proxy.newProxyInstance(apiClientInterface.getClassLoader(),
                new Class<?>[]{apiClientInterface}, new ApiClientInvocationHandler());
    }


    @Override
    public T getObject() throws Exception {
        if (apiClientObject == null) {
            apiClientObject = createProxy();
        }
        return apiClientObject;
    }

    @Override
    public Class<?> getObjectType() {
        return apiClientInterface;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 检查参数
        if(apiClientInterface == null || !apiClientInterface.isInterface()) {
            throw new IllegalArgumentException(apiClientInterface + " must be a interface.");
        }
        if(apiClientObject == null) {
            apiClientObject = createProxy();
        }
    }

}
