package com.sjcnh.mongo.page;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sjcnh.commons.jsonserial.CustomTimeSerializer;
import com.sjcnh.commons.jsonserial.DateDeserializer;

import java.util.Date;
import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: PageExtra
 * @projectName sjcnh-mongo-core
 * @date 2021年05月23日
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public class PageExtra {
    /**
     * 时间的列名或者字段名
     */
    private String columnName;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date beginTime;
    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date endTime;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageExtra pageExtra = (PageExtra) o;
        return Objects.equals(columnName, pageExtra.columnName) && Objects.equals(beginTime, pageExtra.beginTime) && Objects.equals(endTime, pageExtra.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, beginTime, endTime);
    }

    @Override
    public String toString() {
        return "PageExtra{" +
                "columnName='" + columnName + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }
}
