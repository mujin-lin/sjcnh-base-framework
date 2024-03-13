package com.sjcnh.apiclient.annotation;

import com.sjcnh.apiclient.register.ApiClientRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author chenglin.wu
 * @description:
 * @title: EnableApiClients
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ApiClientRegister.class)
public @interface EnableApiClients {

    String[] basePackage() default "";
}
