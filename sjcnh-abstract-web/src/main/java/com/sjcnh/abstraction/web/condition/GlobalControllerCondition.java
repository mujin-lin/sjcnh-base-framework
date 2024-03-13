package com.sjcnh.abstraction.web.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author w
 * @description:
 * @title: CorsCondition
 * @projectName sjcnh-base-framework
 * @date 2023/12/6
 * @company sjcnh-ctu
 */
public class GlobalControllerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Boolean.TRUE.equals(context.getEnvironment().getProperty("sjcnh.web.global.config.enable-global-controller", Boolean.class));
    }
}
