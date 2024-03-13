package com.sjcnh.apiclient.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author chenglin.wu
 * @description:
 * @title: ApiClient
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface ApiClient {

}
