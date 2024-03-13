package com.sjcnh.mongo.page;

import java.util.List;
import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: AbstractPageSearch
 * @projectName sjcnh-mongo-core
 * @date 2023/11/17
 * @company sjcnh-ctu
 */
public abstract class AbstractPageSearch {

    /**
     * 分页信息
     */
    private int current;
    /**
     * 一页展示数量
     */
    private int size;
    /**
     * 排序规则
     */
    private List<PageSort> sortList;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<PageSort> getSortList() {
        return sortList;
    }

    public void setSortList(List<PageSort> sortList) {
        this.sortList = sortList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPageSearch that = (AbstractPageSearch) o;
        return current == that.current && size == that.size && Objects.equals(sortList, that.sortList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, size, sortList);
    }

    @Override
    public String toString() {
        return "AbstractPageSearch{" +
                "current=" + current +
                ", size=" + size +
                ", sortList=" + sortList +
                '}';
    }
}
