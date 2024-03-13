package com.sjcnh.apiclient.poxy;

import com.sjcnh.apiclient.util.SpringContextUtil;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author chenglin.wu
 * @description:
 * @title: ApiClientInvocationHandler
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
public class ApiClientInvocationHandler implements InvocationHandler {
    private final RestTemplate restTemplate = SpringContextUtil.getBean(RestTemplate.class);
    @Override
    public Object invoke(Object poxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();

        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof PostMapping){
                String[] value = ((PostMapping) annotation).value();
                if (ObjectUtils.isEmpty(value)){
                    throw new IllegalArgumentException("request URL not be empty");
                }
                String url = value[0];
                ResponseEntity<?> responseEntity = restTemplate.postForEntity(url, null, returnType);
                Object body = responseEntity.getBody();

            }else if (annotation instanceof GetMapping){
                String[] value = ((GetMapping) annotation).value();
                if (ObjectUtils.isEmpty(value)){
                    throw new IllegalArgumentException("request URL not be empty");
                }
                String url = value[0];
                return restTemplate.getForObject(url, returnType, args);
            }
        }

        return null;
    }
}
