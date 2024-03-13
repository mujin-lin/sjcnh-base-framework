package com.sjcnh.apiclient.register;

import com.sjcnh.apiclient.annotation.EnableApiClients;
import com.sjcnh.apiclient.scan.ApiClientDefinitionScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author chenglin.wu
 * @description:
 * @title: ApiClientRegister
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
public class ApiClientRegister implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        ApiClientDefinitionScanner scanner = new ApiClientDefinitionScanner(beanDefinitionRegistry,false);
        scanner.scan(getBasePackages(annotationMetadata));
    }
    /**
     * 获取需要扫描的基础包
     * @param annotationMetadata the annotationMetadata
     * @return String
     * @author W
     * @date: 2022/2/22
     */
    private String[] getBasePackages(AnnotationMetadata annotationMetadata){
        String[] basePackages = (String[])annotationMetadata.getAnnotationAttributes(EnableApiClients.class.getName()).get("basePackage");
        if (ObjectUtils.isEmpty(basePackages) || !StringUtils.hasText(basePackages[0])){
            String packageName = ClassUtils.getPackageName(annotationMetadata.getClassName());
            return new String[]{packageName};
        }
        return basePackages;
    }
}
