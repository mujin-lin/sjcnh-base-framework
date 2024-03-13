package com.sjcnh.ftp;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;


/**
 * @author chenglin.wu
 * @description:
 * @title: CustomOnPropertyCondition
 * @projectName why-ftp
 * @date 2023/5/30
 * @company sjcnh-ctu
 */
@SuppressWarnings("all")
public class CustomOnPropertyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("file.server.ftp.host");
        return StringUtils.isNotBlank(property);
    }
}
