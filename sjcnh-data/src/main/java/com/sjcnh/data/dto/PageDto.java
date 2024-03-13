package com.sjcnh.data.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Objects;

/**
 * @author chenglin.wu
 * @description:
 * @title: PageDto
 * @projectName why-data
 * @date 2021年05月23日
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public class PageDto<T> {
    /**
     * 系统自带的分页对象
     */
    private Page<T> page;
    /**
     * 查询模板
     */
    private T searchExample;
    /**
     * 扩展字段
     */
    private List<PageExtra> extras;

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public T getSearchExample() {
        return searchExample;
    }

    public void setSearchExample(T searchExample) {
        this.searchExample = searchExample;
    }

    public List<PageExtra> getExtras() {
        return extras;
    }

    public void setExtras(List<PageExtra> extras) {
        this.extras = extras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageDto<?> pageDto = (PageDto<?>) o;
        return Objects.equals(page, pageDto.page) && Objects.equals(searchExample, pageDto.searchExample) && Objects.equals(extras, pageDto.extras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, searchExample, extras);
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "page=" + page +
                ", searchExample=" + searchExample +
                ", extras=" + extras +
                '}';
    }
}
