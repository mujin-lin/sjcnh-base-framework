package com.sjcnh.ftp.service;


import com.sjcnh.ftp.client.FtpServerClient;
import com.sjcnh.ftp.config.FtpConfig;
import com.sjcnh.ftp.constants.FtpConstants;
import com.sjcnh.ftp.dto.FileUploadDto;
import com.sjcnh.ftp.factory.FtpClientFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author chenglin.wu
 * @description:
 * @title: FtpClientService
 * @projectName why-ftp
 * @date 2023/5/12
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public final class FtpClientService {
    /**
     * ftp 连接工厂
     */
    private final FtpClientFactory factory;
    /**
     * ftp 配置
     */
    private final FtpConfig ftpConfig;

    public FtpClientService(FtpClientFactory factory, FtpConfig ftpConfig) {
        this.factory = factory;
        this.ftpConfig = ftpConfig;
    }

    /**
     * 上传文件 默认上传到根目录
     *
     * @param multipartFile 上传的文件
     * @return FileUploadDto
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/5/29
     */
    public FileUploadDto uploadFile(MultipartFile multipartFile) throws IOException, InterruptedException {
        List<FileUploadDto> fileUploadDtos = this.saveFile(null, multipartFile);
        if (fileUploadDtos.isEmpty()) {
            return new FileUploadDto();
        }
        return fileUploadDtos.get(0);
    }

    /**
     * 上传多个文件 默认上传到根目录
     *
     * @param multipartFile 多个文件上传
     * @return FileUploadDto
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/5/29
     */
    public List<FileUploadDto> uploadFile(MultipartFile[] multipartFile) throws IOException, InterruptedException {
        return this.saveFile(null, multipartFile);
    }


    /**
     * 上传文件 上传到根下的指定路径
     *
     * @param uploadDir     上传文件路径
     * @param multipartFile 上传的文件
     * @return FileUploadDto
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/5/29
     */
    public FileUploadDto uploadFile(String uploadDir, MultipartFile multipartFile) throws IOException, InterruptedException {
        FtpServerClient client = this.getClient();
        this.changeWorkDir(uploadDir, client);
        List<FileUploadDto> fileUploadDtos = this.saveFile(client, multipartFile);
        if (fileUploadDtos.isEmpty()) {
            return new FileUploadDto();
        }
        return fileUploadDtos.get(0);
    }

    /**
     * 上传多个文件 上传到根下的指定路径
     *
     * @param uploadDir     上传文件路径
     * @param multipartFile 多个文件上传
     * @return FileUploadDto
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/5/29
     */
    public List<FileUploadDto> uploadFile(String uploadDir, MultipartFile[] multipartFile) throws IOException, InterruptedException {
        FtpServerClient client = this.getClient();
        changeWorkDir(uploadDir, client);
        return this.saveFile(client, multipartFile);
    }

    /**
     * 下载单个文件
     *
     * @param outputStream the outputStream
     * @param fileDir      the fileDir
     * @param fileName     the fileName
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/6/6
     */
    public void download(OutputStream outputStream, String fileDir, String fileName) throws IOException, InterruptedException {
        this.doDownload(outputStream, fileDir, false, this.getClient(), fileName);
    }

    /**
     * 将进来的文件名下载成压缩包
     *
     * @param outputStream the outputStream
     * @param fileDir      the fileDir
     * @param fileNames    the fileNames
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/6/6
     */
    public void downloadZip(OutputStream outputStream, String fileDir, String... fileNames) throws IOException, InterruptedException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            this.downloadFile(byteArrayOutputStream, fileDir, fileNames);
            outputStream.write(byteArrayOutputStream.toByteArray());
        }
    }

    /**
     * 将一个文件夹压缩下载
     *
     * @param outputStream the outputStream
     * @param fileDir      the fileDir
     * @author W
     * @date: 2023/6/6
     */
    public void downDirectory(OutputStream outputStream, String fileDir) throws IOException, InterruptedException {
        FtpServerClient client = this.getClient();
        boolean dirExists = this.dirExists(fileDir, client);
        if (!dirExists) {
            throw new IllegalArgumentException("当前下载路径不存在!");
        }
        client.changeWorkingDirectory(fileDir);
        String[] fileNames = client.listNames();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            this.downloadFile(client, byteArrayOutputStream, fileDir, fileNames);
            outputStream.write(byteArrayOutputStream.toByteArray());
        }
    }

    /**
     * 下载文件
     *
     * @param outputStream the outputStream
     * @throws IOException,InterruptedException IO异常和线程被打断异常
     * @author W
     * @date: 2023/6/2
     */
    private void downloadFile(OutputStream outputStream, String filePath, String... filenames) throws IOException, InterruptedException {

        FtpServerClient client = this.getClient();
        this.doDownload(outputStream, filePath, true, client, filenames);
    }

    /**
     * 下载文件
     *
     * @param outputStream the outputStream
     * @throws IOException,InterruptedException IO异常和线程被打断异常
     * @author W
     * @date: 2023/6/2
     */
    private void downloadFile(FtpServerClient client, OutputStream outputStream, String filePath, String... filenames) throws IOException {
        this.doDownload(outputStream, filePath, true, client, filenames);
    }


    /**
     * 获取ftp连接
     *
     * @return FtpServerClient
     * @throws IOException,InterruptedException io异常和线程打断异常
     * @author W
     * @date: 2023/5/31
     */
    private FtpServerClient getClient() throws IOException, InterruptedException {
        return this.factory.getClient();
    }

    /**
     * 是否使用原始文件名
     *
     * @return boolean
     * @author W
     * @date: 2023/5/31
     */
    private boolean useOriginalFilename() {
        return this.ftpConfig.isKeepOriginalFilename();
    }

    /**
     * 保存文件
     *
     * @param client        the client
     * @param multipartFile the multipartFile
     * @return List<FileUploadDto>
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/6/1
     */
    private List<FileUploadDto> saveFile(FtpServerClient client, MultipartFile... multipartFile) throws IOException, InterruptedException {
        if (ArrayUtils.isEmpty(multipartFile)) {
            throw new IllegalArgumentException("传入文件为空,请检查文件上传是否正常!");
        }
        // 如果传入的client为空则获取一个
        if (ObjectUtils.anyNull(client)) {
            client = this.factory.getClient();
        }
        // 返回list
        List<FileUploadDto> resultList = new ArrayList<>();
        boolean keepOriginal = this.useOriginalFilename();
        try {
            for (MultipartFile file : multipartFile) {
                try (InputStream inputStream = file.getInputStream()) {
                    FileUploadDto result = this.createResult(file, keepOriginal);
                    resultList.add(result);
                    client.storeFile(result.getFtpServerFileName(), inputStream);
                }
            }
        } finally {
            client.disconnect();
        }
        return resultList;
    }


    /**
     * 创建返回结果
     *
     * @param multipartFile the multipartFile
     * @param keepOriginal  the keepOriginal
     * @return FileUploadDto
     * @author W
     * @date: 2023/5/31
     */
    private FileUploadDto createResult(MultipartFile multipartFile, boolean keepOriginal) {
        String serverFileName;
        String originalFilename = multipartFile.getOriginalFilename();

        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setUploadTime(new Date());
        fileUploadDto.setFileContentType(multipartFile.getContentType());
        Assert.notNull(originalFilename, "文件名为空!");
        fileUploadDto.setFilenameExtension(StringUtils.lowerCase(originalFilename.substring(originalFilename.lastIndexOf("."))));
        if (keepOriginal) {
            serverFileName = originalFilename;
        } else {
            serverFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileUploadDto.getFilenameExtension();
        }
        fileUploadDto.setFtpServerFileName(serverFileName);
        fileUploadDto.setOriginalFilename(originalFilename);
        fileUploadDto.setSize(multipartFile.getSize());

        return fileUploadDto;
    }

    /**
     * 切换client的工作路径
     *
     * @param uploadPath the uploadPath
     * @param client     the client
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/5/31
     */
    private void changeWorkDir(String uploadPath, FtpServerClient client) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(FtpConstants.CLIENT_ROOT_DIR);
        uploadPath = this.formatFilePath(uploadPath);
        // 判断目录
        boolean dirExistsAll = dirExists(uploadPath, client);
        if (dirExistsAll) {
            client.changeWorkingDirectory(uploadPath);
            return;
        }
        // 如果目录不存在则切换目录
        String[] allPath = uploadPath.split(FtpConstants.CLIENT_ROOT_DIR);
        for (int i = 1; i < allPath.length; i++) {
            String path = allPath[i];
            if (StringUtils.isBlank(path)) {
                continue;
            }
            String nowPath;
            if (i == 1) {
                nowPath = stringBuilder.append(path).toString();
            } else {
                nowPath = stringBuilder.append(FtpConstants.CLIENT_ROOT_DIR).append(path).toString();
            }
            boolean dirExists = dirExists(nowPath, client);
            if (!dirExists) {
                client.makeDirectory(path);
            }
            client.changeWorkingDirectory(nowPath);
        }
    }

    /**
     * 判断文件夹是否存在
     *
     * @param dir    the dir
     * @param client the client
     * @return boolean
     * @author W
     * @date: 2023/5/31
     */
    private boolean dirExists(String dir, FtpServerClient client) {
        try {
            int cwd = client.cwd(dir);
            return FTPReply.isPositiveCompletion(cwd);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 格式化输入的文件路径
     *
     * @param uploadPath the uploadPath
     * @return String
     * @author W
     * @date: 2023/6/2
     */
    private String formatFilePath(String uploadPath) {
        char firstChar = uploadPath.charAt(0);
        boolean slashFlag = firstChar != (char) 47 && firstChar != (char) 92;
        if (slashFlag) {
            uploadPath = FtpConstants.CLIENT_ROOT_DIR + uploadPath;
        }
        uploadPath = uploadPath.replaceAll("[/|\\\\]+", FtpConstants.CLIENT_ROOT_DIR);
        return uploadPath;
    }

    /**
     * 做下载操作
     *
     * @param outputStream the outputStream
     * @param filePath     the filePath
     * @param zipFlag      the zipFlag
     * @param client       the client
     * @param filenames    the filenames
     * @throws IOException IO异常
     * @author W
     * @date: 2023/6/6
     */
    private void doDownload(OutputStream outputStream, String filePath, boolean zipFlag, FtpServerClient client, String... filenames) throws IOException {
        try {
            filePath = this.formatFilePath(filePath);

            this.downloadCheck(client, outputStream, filePath, filenames);
            if (zipFlag) {
                this.zipFiles(client, outputStream, filenames);
            } else {
                // 不打包则只下载一个文件
                String fileName = filenames[0];
                FTPFile ftpFile = client.getWorkPathFile(fileName);
                boolean directory = ftpFile.isDirectory();
                if (directory) {
                    throw new IllegalArgumentException("请输入文件名!");
                }
                client.retrieveFile(fileName, outputStream);
            }
        } finally {
            client.disconnect();
        }
    }

    /**
     * 下载文件之前的检查
     *
     * @param client       the client
     * @param outputStream the outputStream
     * @param filePath     the filePath
     * @param filenames    the filenames
     * @author W
     * @date: 2023/6/6
     */
    private void downloadCheck(FtpServerClient client, OutputStream outputStream, String filePath, String... filenames) throws IOException {
        boolean dirExists = this.dirExists(filePath, client);
        if (!dirExists) {
            throw new IllegalArgumentException("当前下载路径不存在!");
        }
        // 切换工作路径
        client.changeWorkingDirectory(filePath);
        // 判断传入参数
        if (ArrayUtils.isEmpty(filenames)) {
            throw new IllegalArgumentException("文件名为空!");
        }
        if (ObjectUtils.anyNull(outputStream)) {
            throw new IllegalArgumentException("没有输出流!");
        }
    }

    /**
     * 下载压缩文件
     *
     * @param client       the client
     * @param outputStream the outputStream
     * @param filenames    the filenames
     * @author W
     * @date: 2023/6/6
     */
    private void zipFiles(FtpServerClient client, OutputStream outputStream, String[] filenames) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        this.zipFile(client, filenames, zipOutputStream, "");
        zipOutputStream.close();
    }

    /**
     * 压缩文件
     *
     * @param client          the client
     * @param filenames       the filenames
     * @param zipOutputStream the zipOutputStream
     * @param filePrefix      the filePrefix
     * @throws IOException IO异常
     * @author W
     * @date: 2023/6/6
     */
    private void zipFile(FtpServerClient client, String[] filenames, ZipOutputStream zipOutputStream, String filePrefix) throws IOException {
        Map<String, AtomicInteger> fileMap = new HashMap<>();
        for (String filename : filenames) {
            boolean fileExists = client.remoteRetrieve(filename);
            if (fileExists) {
                throw new IllegalArgumentException("当前文件：" + filename + "不存在");
            }
            // 判断当前是文件还是文件夹
            FTPFile ftpFile = client.getWorkPathFile(filename);
            boolean isDirectory = ftpFile.isDirectory();
            if (isDirectory) {
                this.zipDir(ftpFile.getName(), client, zipOutputStream);
                continue;
            }
            // 将文件放入压缩流
            String replaceFileName = filePrefix + filename;
            if (fileMap.containsKey(filename)) {
                AtomicInteger atomicInteger = fileMap.get(filename);
                int i = atomicInteger.incrementAndGet();
                int pointIndex = filename.lastIndexOf(".");
                replaceFileName = filePrefix + String.format("%s(%02d)%s", filename.substring(0, pointIndex), i, filename.substring(pointIndex));
            } else {
                fileMap.put(replaceFileName, new AtomicInteger(0));
            }
            // 添加压缩信息
            zipOutputStream.putNextEntry(new ZipEntry(replaceFileName));
            client.retrieveFile(filename, zipOutputStream);
        }
    }

    /**
     * 检测到文件夹切换文件目录
     *
     * @param dirPath         the dirPath
     * @param client          the client
     * @param zipOutputStream the zipOutputStream
     * @throws IOException IO异常
     * @author W
     * @date: 2023/6/6
     */
    private void zipDir(String dirPath, FtpServerClient client, ZipOutputStream zipOutputStream) throws IOException {
        String currentWorkingDir = client.printWorkingDirectory();
        client.changeWorkingDirectory(dirPath);

        String[] fileNames = client.listNames();
        this.zipFile(client, fileNames, zipOutputStream, dirPath + File.separator);
        client.changeWorkingDirectory(currentWorkingDir);
    }

}
