package com.sjcnh.ftp.dto;

import java.util.Date;
import java.util.Objects;

/**
 * @author chenglin.wu
 * @description:
 * @title: FileUploadDto
 * @projectName why-ftp
 * @date 2023/5/29
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public class FileUploadDto {
    /**
     * 实际上传的名字
     */
    private String originalFilename;
    /**
     * ftp服务器保存的文件名
     */
    private String ftpServerFileName;
    /**
     * 文件扩展名
     */
    private String fileContentType;
    /**
     * 文件上传时间
     */
    private Date uploadTime;
    /**
     * 大小
     */
    private Long size;

    private String filenameExtension;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFtpServerFileName() {
        return ftpServerFileName;
    }

    public void setFtpServerFileName(String ftpServerFileName) {
        this.ftpServerFileName = ftpServerFileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFilenameExtension() {
        return filenameExtension;
    }

    public void setFilenameExtension(String filenameExtension) {
        this.filenameExtension = filenameExtension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileUploadDto that = (FileUploadDto) o;
        return Objects.equals(originalFilename, that.originalFilename) && Objects.equals(ftpServerFileName, that.ftpServerFileName) && Objects.equals(fileContentType, that.fileContentType) && Objects.equals(uploadTime, that.uploadTime) && Objects.equals(size, that.size) && Objects.equals(filenameExtension, that.filenameExtension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalFilename, ftpServerFileName, fileContentType, uploadTime, size, filenameExtension);
    }

    @Override
    public String toString() {
        return "FileUploadDto{" +
                "originalFilename='" + originalFilename + '\'' +
                ", ftpServerFileName='" + ftpServerFileName + '\'' +
                ", fileContentType='" + fileContentType + '\'' +
                ", uploadTime=" + uploadTime +
                ", size=" + size +
                ", filenameExtension='" + filenameExtension + '\'' +
                '}';
    }
}
