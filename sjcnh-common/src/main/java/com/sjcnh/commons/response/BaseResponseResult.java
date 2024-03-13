package com.sjcnh.commons.response;

import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: BaseResponseResult
 * @projectName sjcnh-base-framework
 * @date 2024/1/4
 * @company sjcnh-ctu
 */
public class BaseResponseResult {
    /**
     * 返回代码
     */
    private int resCode;

    /**
     * 返回信息
     */
    private String resMsg;
    /**
     * 返回详细信息
     */
    private String detailMsg;

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    /**
     * @param resMsg the resMsg to set
     */
    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseResponseResult that = (BaseResponseResult) o;
        return resCode == that.resCode && Objects.equals(resMsg, that.resMsg) && Objects.equals(detailMsg, that.detailMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resCode, resMsg, detailMsg);
    }

    @Override
    public String toString() {
        return "BaseResponseResult{" +
                "resCode=" + resCode +
                ", resMsg='" + resMsg + '\'' +
                ", detailMsg='" + detailMsg + '\'' +
                '}';
    }
}
