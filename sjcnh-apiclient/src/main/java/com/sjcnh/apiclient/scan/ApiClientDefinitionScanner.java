package com.sjcnh.apiclient.scan;

import com.sjcnh.apiclient.annotation.ApiClient;
import com.sjcnh.apiclient.factory.ApiClientFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @author chenglin.wu
 * @description:
 * @title: ApiClientDefinitionScanner
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
public class ApiClientDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public ApiClientDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
        super.addIncludeFilter(new AnnotationTypeFilter(ApiClient.class));
    }

    /**
     * 默认情况下只有顶层具体类才会通过
     * 只返回是接口的beanDefinition
     * @param beanDefinition bean
     * @return true / false
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface()
                && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : holders) {
            convertApiClientFactoryBean(holder.getBeanDefinition());
        }
        return holders;
    }

    private void convertApiClientFactoryBean(BeanDefinition beanDefinition){
        GenericBeanDefinition apiClientFactoryDefinition = (GenericBeanDefinition) beanDefinition;
        String interfaceName = beanDefinition.getBeanClassName();
        apiClientFactoryDefinition.setBeanClass(ApiClientFactoryBean.class);
        apiClientFactoryDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0,interfaceName);

    }


}
