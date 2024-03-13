package com.sjcnh.data.configuration;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenglin.wu
 * @description:
 * @title: DataAutoConfiguration
 * @projectName why-data
 * @date 2023/6/26
 * @company sjcnh-ctu
 */
@Configuration
@AutoConfigureAfter(SjcnhDataConfig.class)
public class DataAutoConfiguration {

    /**
     *
     * @param config the config
     * @return MybatisPlusInterceptor
     * @author W
     * @date: 2023/6/26
     */
    @Bean
    @ConditionalOnBean(SjcnhDataConfig.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(SjcnhDataConfig config) {
        return MybatisPlusInterceptorBuilder.create().dbType(config.getDbType()).openPage(config.isOpenPageInterceptor())
                .openOptimisticLocker(config.isOptimisticLocker()).openBlockAttackInner(config.isBlockAttackInner()).build();
    }

    /**
     * @author chenglin.wu
     * @description:
     * @title: MybatisPlusInterceptorBuilder
     * @projectName why-data
     * @date 2023/6/26
     * @company sjcnh-ctu
     */
    static class MybatisPlusInterceptorBuilder {
        private final MybatisPlusInterceptor interceptor;
        private boolean openPage;

        private DbType dbType;


        private MybatisPlusInterceptorBuilder(MybatisPlusInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        protected static MybatisPlusInterceptorBuilder create() {
            return new MybatisPlusInterceptorBuilder(new MybatisPlusInterceptor());
        }

        protected MybatisPlusInterceptorBuilder openPage(boolean openPage) {
            this.openPage = openPage;
            return this;
        }

        protected MybatisPlusInterceptorBuilder openOptimisticLocker(boolean openOptimisticLocker) {
            if (openOptimisticLocker) {
                this.interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
            }
            return this;
        }

        protected MybatisPlusInterceptorBuilder openBlockAttackInner(boolean blockAttackInner) {
            if (blockAttackInner) {
                this.interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
            }
            return this;
        }

        protected MybatisPlusInterceptorBuilder dbType(DbType dbType) {
            this.dbType = dbType;
            return this;
        }

        protected MybatisPlusInterceptor build() {
            if (this.openPage) {
                PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
                if (this.dbType != null) {
                    paginationInnerInterceptor.setDbType(dbType);
                }
                this.interceptor.addInnerInterceptor(paginationInnerInterceptor);
            }
            return this.interceptor;
        }

    }
}
