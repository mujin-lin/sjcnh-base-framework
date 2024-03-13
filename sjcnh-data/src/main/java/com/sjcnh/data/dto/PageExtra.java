package com.sjcnh.data.dto;



import com.sjcnh.commons.constants.ReflectConstants;

import java.util.Objects;

/**
 * @author chenglin.wu
 * @description:
 * @title: PageExtraI
 * @projectName why-data
 * @date 2021年05月23日
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public class PageExtra {
    /**
     * 范围查询的实体类字段名
     */
    private String columnName;

    /**
     * 左边区间的值
     */
    private Object beginValue;
    /**
     * 右边区间的值
     */
    private Object endValue;
    /**
     * 左边是否是关区间
     */
    private boolean closeLeft;
    /**
     * 右边是否是关区间
     */
    private boolean closeRight;

    /**
     * 判断是否是全闭合区间
     *
     * @return boolean
     */
    private boolean closeAllSection() {
        return this.closeLeft && this.closeRight;
    }

    private boolean openLeftAndCloseRight(){
        return !this.closeLeft && this.closeRight;
    }

    private boolean closeLeftAndOpenRight(){
        return this.closeLeft && !this.closeRight;
    }

    private boolean openAll(){
        return !this.closeLeft && !this.closeRight;
    }

    /**
     * 判断区间
     * @return 区间的值
     */
    public String judgmentSection(){
        if (this.closeAllSection()){
            return ReflectConstants.CLOSE_ALL;
        }
        if (this.openLeftAndCloseRight()){
            return ReflectConstants.OPEN_LEFT_AND_CLOSE_RIGHT;
        }
        if (this.closeLeftAndOpenRight()){
            return ReflectConstants.CLOSE_LEFT_AND_OPEN_RIGHT;
        }
        return ReflectConstants.OPEN_ALL;
    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getBeginValue() {
        return beginValue;
    }

    public void setBeginValue(Object beginValue) {
        this.beginValue = beginValue;
    }

    public Object getEndValue() {
        return endValue;
    }

    public void setEndValue(Object endValue) {
        this.endValue = endValue;
    }

    public boolean isCloseLeft() {
        return closeLeft;
    }

    public void setCloseLeft(boolean closeLeft) {
        this.closeLeft = closeLeft;
    }

    public boolean isCloseRight() {
        return closeRight;
    }

    public void setCloseRight(boolean closeRight) {
        this.closeRight = closeRight;
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
        return closeLeft == pageExtra.closeLeft && closeRight == pageExtra.closeRight && Objects.equals(columnName, pageExtra.columnName) && Objects.equals(beginValue, pageExtra.beginValue) && Objects.equals(endValue, pageExtra.endValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, beginValue, endValue, closeLeft, closeRight);
    }

    @Override
    public String toString() {
        return "PageExtra{" +
                "columnName='" + columnName + '\'' +
                ", beginValue=" + beginValue +
                ", endValue=" + endValue +
                ", closeLeft=" + closeLeft +
                ", closeRight=" + closeRight +
                '}';
    }
}
