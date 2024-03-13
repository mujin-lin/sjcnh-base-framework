
package com.sjcnh.commons.response;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: ResponseResult
 * @projectName sjcnh-common
 * @date 2021/4/16
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public class ResponseResult<T> extends BaseResponseResult implements Serializable {

    /**
     * 返回数据
     */
    private T resData;



    public T getResData() {
        return resData;
    }

    public void setResData(T resData) {
        this.resData = resData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResponseResult<?> that = (ResponseResult<?>) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(resData, that.resData).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(resData).toHashCode();
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "resData=" + resData +
                '}'+super.toString();
    }
}
