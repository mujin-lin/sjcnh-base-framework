package com.sjcnh.ftp.client;


import com.sjcnh.ftp.DisconnectionListener;
import com.sjcnh.ftp.constants.FtpConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author chenglin.wu
 * @description:
 * @title: FtpServerClient
 * @projectName why-ftp
 * @date 2023/5/18
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public class FtpServerClient extends FTPClient {

    private DisconnectionListener listener;

    public FtpServerClient() {
    }

    public FtpServerClient(DisconnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void disconnect() throws IOException {
        listener.disconnection(this);
    }

    /**
     * @throws IOException 异常
     * @author W
     * @date: 2023/5/30
     */
    public void close() throws IOException {
        super.logout();
        super.disconnect();
    }


    /**
     * 再次借出时初始化client
     *
     * @author W
     * @date: 2023/5/18
     */
    public void initClient() throws IOException {
        this.switchRootDirectory();

    }

    /**
     * 切换工目录到根目录
     *
     * @author W
     * @date: 2023/5/18
     */
    public void switchRootDirectory() throws IOException {
        // 判断当前目录是否是工作根目录
        String nowDirectory = super.printWorkingDirectory();
        // 工作根目录
        if (!FtpConstants.CLIENT_ROOT_DIR.equals(nowDirectory)) {
            super.changeWorkingDirectory(FtpConstants.CLIENT_ROOT_DIR);
        }
    }

    /**
     * 获取当前路径下的指定文件名的文件
     *
     * @param fileName the fileName
     * @return FTPFile
     * @author W
     * @date: 2023/6/6
     */
    public FTPFile getWorkPathFile(String fileName) throws IOException {
        FTPFile[] ftpFiles = super.listFiles(fileName);
        if (!ArrayUtils.isEmpty(ftpFiles)) {
            // 如果找的是文件
            for (FTPFile ftpFile : ftpFiles) {
                if (fileName.equals(ftpFile.getName())){
                    return ftpFile;
                }
            }
            // 如果输入的是文件夹
            FTPFile[] directories = super.listDirectories(this.printWorkingDirectory());
            if (ArrayUtils.isNotEmpty(directories)){
                for (FTPFile directory : directories) {
                    if (directory.getName().equals(fileName)){
                        return directory;
                    }
                }
            }

        }
        throw new FileNotFoundException("current file not exists!");
    }
}
