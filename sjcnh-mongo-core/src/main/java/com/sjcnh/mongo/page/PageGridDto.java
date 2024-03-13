package com.sjcnh.mongo.page;

import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: PageGridDto
 * @projectName sjcnh-mongo-core
 * @date 2023/11/16
 * @company sjcnh-ctu
 */
public class PageGridDto extends AbstractPageSearch{

    /**
     * 查询模板
     */
    private GirdFileExampleDto searchExample;

    public GirdFileExampleDto getSearchExample() {
        return searchExample;
    }

    public void setSearchExample(GirdFileExampleDto searchExample) {
        this.searchExample = searchExample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PageGridDto that = (PageGridDto) o;
        return Objects.equals(searchExample, that.searchExample);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), searchExample);
    }

    @Override
    public String toString() {
        return "PageGridDto{" +
                "searchExample=" + searchExample +
                '}';
    }
}
