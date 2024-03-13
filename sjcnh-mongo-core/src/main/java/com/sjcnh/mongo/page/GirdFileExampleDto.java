package com.sjcnh.mongo.page;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sjcnh.commons.jsonserial.CustomTimeSerializer;
import com.sjcnh.commons.jsonserial.DateDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: GirdFileExampleDto
 * @projectName sjcnh-mongo-core
 * @date 2023/11/16
 * @company sjcnh-ctu
 */
public class GirdFileExampleDto {
    /**
     * 搜搜的id集合
     */
    private List<String> ids;
    /**
     * 文件名 支持模糊查询
     */
    private String filename;
    /**
     * 上传时间开始
     */
    @JsonSerialize(using = CustomTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date uploadDateStart;
    /**
     * 上传时间结束
     */
    @JsonSerialize(using = CustomTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date uploadDateEnd;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getUploadDateStart() {
        return uploadDateStart;
    }

    public void setUploadDateStart(Date uploadDateStart) {
        this.uploadDateStart = uploadDateStart;
    }

    public Date getUploadDateEnd() {
        return uploadDateEnd;
    }

    public void setUploadDateEnd(Date uploadDateEnd) {
        this.uploadDateEnd = uploadDateEnd;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GirdFileExampleDto that = (GirdFileExampleDto) o;
        return Objects.equals(ids, that.ids) && Objects.equals(filename, that.filename) && Objects.equals(uploadDateStart, that.uploadDateStart) && Objects.equals(uploadDateEnd, that.uploadDateEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids, filename, uploadDateStart, uploadDateEnd);
    }

    @Override
    public String toString() {
        return "GirdFileExampleDto{" +
                "ids=" + ids +
                ", filename='" + filename + '\'' +
                ", uploadDateStart=" + uploadDateStart +
                ", uploadDateEnd=" + uploadDateEnd +
                '}';
    }
}
