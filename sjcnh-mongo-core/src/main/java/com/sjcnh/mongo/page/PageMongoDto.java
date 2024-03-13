package com.sjcnh.mongo.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sjcnh.mongo.doc.BaseDocument;
import com.sjcnh.mongo.utils.QueryUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: PageDto
 * @projectName sjcnh-mongo-core
 * @date 2023/10/18
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public class PageMongoDto<T extends BaseDocument> extends AbstractPageSearch{
    @JsonIgnore
    private Class<T> searchClass;
    /**
     * 查询模板
     */
    private T searchExample;
    /**
     * 扩展字段
     */
    private List<PageExtra> pageExtras;

    public PageMongoDto() {

    }

    public Class<T> getSearchClass() {
        return searchClass;
    }

    public void setSearchClass(Class<T> searchClass) {
        this.searchClass = searchClass;
    }

    public T getSearchExample() {
        return searchExample;
    }

    public void setSearchExample(T searchExample) {
        this.searchExample = searchExample;
    }

    public List<PageExtra> getPageExtras() {
        return pageExtras;
    }

    public void setPageExtras(List<PageExtra> pageExtras) {
        this.pageExtras = pageExtras;
    }

    /**
     * 获取分页请求对象
     *
     * @return PageRequest
     * @author W
     * @date: 2023/10/20
     */
    public PageRequest getPageRequest() {
        if (CollectionUtils.isEmpty(super.getSortList())){
            return PageRequest.of(super.getCurrent(), super.getSize());
        }
        Sort sort = QueryUtils.createSort(super.getSortList());
        return PageRequest.of(super.getCurrent(),super.getSize(),sort);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PageMongoDto<?> that = (PageMongoDto<?>) o;
        return Objects.equals(searchClass, that.searchClass) && Objects.equals(searchExample, that.searchExample) && Objects.equals(pageExtras, that.pageExtras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), searchClass, searchExample, pageExtras);
    }

    @Override
    public String toString() {
        return "PageMongoDto{" +
                "searchClass=" + searchClass +
                ", searchExample=" + searchExample +
                ", pageExtras=" + pageExtras +
                '}';
    }
}
