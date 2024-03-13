package com.sjcnh.mongo.doc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sjcnh.commons.jsonserial.CustomTimeSerializer;
import com.sjcnh.commons.jsonserial.DateDeserializer;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author w
 * @description:
 * @title: BaseDocument
 * @projectName sjcnh-mongo-core
 * @date 2023/10/18
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public abstract class BaseDocument<ID extends Serializable> {

    @Field("create_time")
    @CreatedDate
//    @JsonSerialize(using = CustomTimeSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime createTime;

    @Field("update_time")
    @LastModifiedDate
//    @JsonSerialize(using = CustomTimeSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime updateTime;

    @Field("remark")
    private String remark;

    /**
     * 获取当前数据实体的id
     *
     * @return Serializable
     * @author W
     * @date: 2023/10/18
     */
    public abstract ID getId();

    /**
     * 设置当前实体的id
     *
     * @param id 当前实体id
     * @author W
     * @date: 2023/10/18
     */
    public abstract void setId(ID id);

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public <T> void initDelFlag() {
       this.initDelFlag(null,null);
    }

    /**
     * 初始化del flag
     *
     * @param consumer the consumer
     * @param value    the value
     * @return void
     * @author W
     * @date: 2023/10/24
     */
    public <T> void initDelFlag(Consumer<T> consumer, T value) {
        if (ObjectUtils.anyNull(consumer,value)){
            return;
        }
        consumer.accept(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDocument<?> that = (BaseDocument<?>) o;
        return Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, updateTime);
    }

    @Override
    public String toString() {
        return "BaseDocument{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
