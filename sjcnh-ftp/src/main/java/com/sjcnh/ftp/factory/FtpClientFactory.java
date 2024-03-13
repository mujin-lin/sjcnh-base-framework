package com.sjcnh.ftp.factory;


import com.sjcnh.ftp.CreateClientListener;
import com.sjcnh.ftp.DisconnectionListener;
import com.sjcnh.ftp.client.FtpServerClient;
import com.sjcnh.ftp.config.FtpConfig;
import com.sjcnh.ftp.constants.FtpConstants;
import com.sjcnh.ftp.pool.FtpSource;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenglin.wu
 * @description:
 * @title: FtpClientFactory
 * @projectName why-ftp
 * @date 2023/5/12
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public class FtpClientFactory {

    private final Logger log = LoggerFactory.getLogger(FtpClientFactory.class);
    /**
     * ftp 配置类
     */
    private final FtpConfig ftpConfig;
    /**
     * 连接池依赖
     */
    private FtpSource ftpSource;
    /**
     * 创建client的监听
     */
    private final CreateClientListener createClientListener;


    public FtpClientFactory(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
        this.createClientListener = new CreateClientHandler();
    }

    /**
     * 获取连接
     *
     * @return FtpServerClient
     * @throws IOException,InterruptedException 异常信息
     * @author W
     * @date: 2023/5/30
     */
    public FtpServerClient getClient() throws IOException, InterruptedException {
        return this.ftpSource.getClient();
    }


    /**
     * 项目启动时初始化线程池
     *
     * @return List<FtpServerClient>
     * @throws InterruptedException,ExecutionException 异常信息
     * @author W
     * @date: 2023/5/30
     */
    public List<FtpServerClient> initClient() throws InterruptedException, ExecutionException {
        // 创建线程list对象和返回数据的集合
        List<InitFtpClient> initFtpClients = new ArrayList<>();
        List<FtpServerClient> initResult = new ArrayList<>();
        // 创建线程池并创建调用初始化的线程 初始化client
        int initSize = this.ftpConfig.getInitSize();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(initSize, this.ftpConfig.getMaxSize(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new CreateFtpThreadFactory());
        for (int i = 0; i < initSize; i++) {
            initFtpClients.add(new InitFtpClient());
        }
        List<Future<FtpServerClient>> futures = executor.invokeAll(initFtpClients);
        // 获取线程初始化结果，并返回数据
        for (Future<FtpServerClient> future : futures) {
            FtpServerClient ftpServerClient = future.get();
            initResult.add(ftpServerClient);
        }
        // 任务结束后将线程池关闭
        executor.shutdown();
        return initResult;
    }

    /**
     * 设置池子在factory中
     *
     * @param ftpSource the ftpSource
     * @author W
     * @date: 2023/5/31
     */
    public void setFtpSource(FtpSource ftpSource) {
        this.ftpSource = ftpSource;
    }

    /**
     * 归还client
     *
     * @param client the client
     * @author W
     * @date: 2023/5/30
     */
    public void returnClient(FtpServerClient client) throws IOException {
        client.initClient();
        this.ftpSource.restore(client);
    }

    /**
     * 创建client
     *
     * @return FtpServerClient
     * @throws IOException 异常
     * @author W
     * @date: 2023/5/30
     */
    private FtpServerClient createClient() throws IOException {
        InitFtpClient initFtpClient = new InitFtpClient();
        return initFtpClient.call();
    }

    /**
     * 验证对象是否存活
     *
     * @param client the client
     * @return boolean
     * @author W
     * @date: 2023/5/31
     */
    private boolean validateObject(FtpServerClient client) throws IOException {
        return client.sendNoOp();
    }

    /**
     * 获取配置的最大连接数
     *
     * @return int
     * @author W
     * @date: 2023/5/31
     */
    public int getMaxPoolSize() {
        return this.ftpConfig.getMaxSize();
    }

    /**
     * 获取配置的检测间隔数 毫秒
     *
     * @return int
     * @author W
     * @date: 2023/5/31
     */
    public int getIntervalCheck() {
        return this.ftpConfig.getCheckInterval();
    }

    /**
     * 获取当前
     *
     * @return CreateClientListener
     * @author W
     * @date: 2023/5/31
     */
    public CreateClientListener getCreateClientListener() {
        return this.createClientListener;
    }

    /**
     * @author chenglin.wu
     * @description: 初始化连接的class
     * @title: InitFtpClient
     * @projectName why-ftp
     * @date 2023/5/15
     * @company sjcnh-ctu
     */
    class InitFtpClient implements Callable<FtpServerClient> {

        public InitFtpClient() {

        }

        @Override
        public FtpServerClient call() throws IOException {
            return initClient();
        }

        /**
         * 初始化数据
         *
         * @author W
         * @date: 2023/5/29
         */
        private FtpServerClient initClient() throws IOException {

            FtpServerClient ftpServerClient = new FtpServerClient(new DisconnectionHandler());

            ftpServerClient.setAutodetectUTF8(ftpConfig.isAutodetectUtf8());
            ftpServerClient.connect(ftpConfig.getHost(), ftpConfig.getPort());
            ftpServerClient.login(ftpConfig.getUserName(), ftpConfig.getPassword());
            int replyCode = ftpServerClient.getReplyCode();
            ftpServerClient.setConnectTimeout(0);
            ftpServerClient.setBufferSize(ftpConfig.getClientBufferSize());

            if (FTPReply.isPositiveCompletion(replyCode)) {
                ftpServerClient.setFileType(FtpServerClient.BINARY_FILE_TYPE);
                ftpServerClient.enterLocalPassiveMode();
            }
            Charset charset = StandardCharsets.UTF_8;
            // 如果使用默认UTF8的字符编码则发送opts 设置
            if (StandardCharsets.UTF_8.equals(ftpConfig.getControlEncoding())) {
                ftpServerClient.sendCommand(FtpConstants.USE_UTF8_COMMAND, FtpConstants.USE_UTF8_COMMAND_ON);
            } else {
                charset = ftpConfig.getCharset();
            }
            ftpServerClient.setCharset(charset);
            ftpServerClient.setControlEncoding(charset.name());

            return ftpServerClient;
        }
    }

    /**
     * @author chenglin.wu
     * @description: client 调用disconnection时的监听
     * @title: CreateFtpThreadFactory
     * @projectName why-ftp
     * @date 2023/5/15
     * @company sjcnh-ctu
     */
    class DisconnectionHandler implements DisconnectionListener {

        @Override
        public void disconnection(FtpServerClient client) throws IOException {
            client.initClient();
            ftpSource.restore(client);
        }
    }

    /**
     * @author chenglin.wu
     * @description: 池子会持有当前对象，在将所有不可用的连接删除完了之后会调用此处进行创建client
     * @title: CreateClientHandler
     * @projectName why-ftp
     * @date 2023/5/15
     * @company sjcnh-ctu
     */
    class CreateClientHandler implements CreateClientListener {

        @Override
        public FtpServerClient createClient() throws IOException {
            return FtpClientFactory.this.createClient();
        }
    }


    /**
     * @author chenglin.wu
     * @description: 创建线程池的factory 用于更改线程名
     * @title: CreateFtpThreadFactory
     * @projectName why-ftp
     * @date 2023/5/15
     * @company sjcnh-ctu
     */
    static class CreateFtpThreadFactory implements ThreadFactory {

        private final AtomicInteger atomicInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            String name = "Create FtpClient thread" + atomicInteger.getAndIncrement();
            return new Thread(null, runnable, name, 0);
        }
    }

}
