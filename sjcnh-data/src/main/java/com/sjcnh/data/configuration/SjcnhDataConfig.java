package com.sjcnh.data.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author chenglin.wu
 * @description:
 * @title: WhyDataConfig
 * @projectName why-data
 * @date 2023/6/26
 * @company sjcnh-ctu
 */
@Configuration
@ConfigurationProperties("sjcnh.data")
public class SjcnhDataConfig {
    /**
     * 分页插件类型
     */
    private DbType dbType;
    /**
     * 是否开启乐观锁插件
     */
    private boolean optimisticLocker;
    /**
     * 是否开启防全表更新与删除插件
     */
    private boolean blockAttackInner;
    /**
     * 开启分页插件拦截
     */
    private boolean openPageInterceptor=true;

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public boolean isOptimisticLocker() {
        return optimisticLocker;
    }

    public void setOptimisticLocker(boolean optimisticLocker) {
        this.optimisticLocker = optimisticLocker;
    }

    public boolean isBlockAttackInner() {
        return blockAttackInner;
    }

    public void setBlockAttackInner(boolean blockAttackInner) {
        this.blockAttackInner = blockAttackInner;
    }

    public boolean isOpenPageInterceptor() {
        return openPageInterceptor;
    }

    public void setOpenPageInterceptor(boolean openPageInterceptor) {
        this.openPageInterceptor = openPageInterceptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SjcnhDataConfig that = (SjcnhDataConfig) o;
        return optimisticLocker == that.optimisticLocker && blockAttackInner == that.blockAttackInner && openPageInterceptor == that.openPageInterceptor && dbType == that.dbType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbType, optimisticLocker, blockAttackInner, openPageInterceptor);
    }
}
