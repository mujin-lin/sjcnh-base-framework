package com.sjcnh.mongo.page;

import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: PageSort
 * @projectName sjcnh-mongo-core
 * @date 2023/11/17
 * @company sjcnh-ctu
 */
public class PageSort {
    /**
     * 排序的列名
     */
    private String columnName;
    /**
     * 排序规则，允许 desc 和 asc 为空则默认asc
     */
    private String sortBy;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageSort pageSort = (PageSort) o;
        return Objects.equals(columnName, pageSort.columnName) && Objects.equals(sortBy, pageSort.sortBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, sortBy);
    }

    @Override
    public String toString() {
        return "PageSort{" +
                "columnName='" + columnName + '\'' +
                ", sortBy='" + sortBy + '\'' +
                '}';
    }
}
