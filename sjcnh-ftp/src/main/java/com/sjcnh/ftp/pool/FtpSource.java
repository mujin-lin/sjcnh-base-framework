package com.sjcnh.ftp.pool;


import com.sjcnh.ftp.CreateClientListener;
import com.sjcnh.ftp.client.FtpServerClient;
import com.sjcnh.ftp.factory.FtpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenglin.wu
 * @description:
 * @title: FtpSource
 * @projectName why-ftp
 * @date 2023/5/15
 * @company sjcnh-ctu
 */
@SuppressWarnings("all")
public class FtpSource implements DisposableBean {

    private final Logger log = LoggerFactory.getLogger(FtpSource.class);
    /**
     * 所有的连接client
     */
    private final BlockingDeque<FtpServerClient> allClient;
    /**
     * 当前
     */
    private final AtomicInteger currentPoolSize;
    /**
     * 保持连接活跃的类
     */
    private final KeepAlive keepAlive;
    /**
     * 创建client监听
     */
    private final CreateClientListener createClientListener;
    /**
     * 线程检测和获取线程时的锁
     */
    private final Lock lock = new ReentrantLock();
    /**
     * 池子最大容量
     */
    private final int maxSize;


    public FtpSource(FtpClientFactory clientFactory) throws ExecutionException, InterruptedException {
        this.maxSize = clientFactory.getMaxPoolSize();
        this.allClient = new LinkedBlockingDeque<>(this.maxSize);
        this.createClientListener = clientFactory.getCreateClientListener();
        List<FtpServerClient> ftpServerClients = clientFactory.initClient();
        this.currentPoolSize = new AtomicInteger(ftpServerClients.size());

        clientFactory.setFtpSource(this);

        this.allClient.addAll(ftpServerClients);
        // 检测活跃线程开启
        this.keepAlive = new KeepAlive(clientFactory.getMaxPoolSize());
        this.keepAlive.startAliveTest(clientFactory.getIntervalCheck());
    }

    /**
     * 获取client
     *
     * @return FtpServerClient
     * @throws InterruptedException 线程被打断异常
     * @author W
     * @date: 2023/5/18
     */
    public FtpServerClient getClient() throws InterruptedException, IOException {
        lock.lock();
        try {
            int currentPoolSize2Queue = this.nowClientNum();
            if (this.currentPoolSize.intValue() <= 0 && this.currentPoolSize.intValue() < this.maxSize && currentPoolSize2Queue <= 0) {
                this.currentPoolSize.incrementAndGet();
                return this.createClientListener.createClient();
            }
            return this.allClient.take();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取当前连接池中的client数量
     *
     * @return int
     * @author W
     * @date: 2023/5/31
     */
    public int nowClientNum() {
        return this.allClient.size();
    }


    /**
     * 获取并添加一次当前最大容量
     *
     * @return int
     * @author W
     * @date: 2023/5/30
     */
    public int incrMaxPoolSize() {
        return this.currentPoolSize.incrementAndGet();
    }

    /**
     * 获取并添加一次当前最大容量
     *
     * @return int
     * @author W
     * @date: 2023/5/30
     */
    public int decrMaxPoolSize() {
        return this.currentPoolSize.decrementAndGet();
    }

    /**
     * 归还重新入队的方法
     *
     * @param client the client
     * @author W
     * @date: 2023/5/30
     */
    public void restore(FtpServerClient client) {
        this.allClient.addLast(client);
    }

    /**
     * 线程关闭之前清除连接
     *
     * @author W
     * @date: 2023/5/30
     */
    private void destroyAll() throws IOException {
        this.keepAlive.stop();
        for (FtpServerClient ftpServerClient : this.allClient) {
            ftpServerClient.close();
        }
        this.allClient.clear();
    }


    @Override
    public void destroy() throws Exception {
        try {
            this.destroyAll();
        } catch (IOException e) {
            log.error("关闭 ftp 连接出错：", e);
        }
    }

    /**
     * @author chenglin.wu
     * @description: 定时执行的类
     * @title: KeepAliveThread
     * @projectName why-ftp
     * @date 2023/5/15
     * @company sjcnh-ctu
     */
    class KeepAlive {
        private final ScheduledExecutorService executor;

        private KeepAlive(int maxSize) {
            this.executor = new ScheduledThreadPoolExecutor(maxSize, new KeepAliveThreadFactory());
        }

        /**
         * 开启定时任务检测client活跃度
         *
         * @author W
         * @date: 2023/5/31
         */
        private void startAliveTest(int intervalCheck) {
            executor.scheduleWithFixedDelay(new KeepAliveThread(), intervalCheck, intervalCheck, TimeUnit.MILLISECONDS);
        }

        /**
         * 停止检测线程
         *
         * @author W
         * @date: 2023/5/31
         */
        private void stop() {
            executor.shutdown();
        }
    }

    /**
     * @author chenglin.wu
     * @description: 让 client 保持活跃的线程执行任务
     * @title: KeepAliveThread
     * @projectName why-ftp
     * @date 2023/5/15
     * @company sjcnh-ctu
     */
    class KeepAliveThread implements Runnable {
        /**
         * 检测client活跃度的方法
         *
         * @author W
         * @date: 2023/5/31
         */
        @Override
        public void run() {
            // 检测有问题的client
            List<FtpServerClient> removeClients = new ArrayList<>();
            for (FtpServerClient ftpServerClient : allClient) {
                try {
                    boolean aliveFlag = ftpServerClient.sendNoOp();
                    if (!aliveFlag) {
                        removeClients.add(ftpServerClient);
                    }
                } catch (IOException e) {
                    log.error("check alive error:", e);
                    removeClients.add(ftpServerClient);
                }
            }
            if (removeClients.isEmpty()) {
                return;
            }
            // 加锁
            lock.lock();
            try {
                // 如果需要移除的数据不为空就执行删除
                for (FtpServerClient removeClient : removeClients) {
                    allClient.remove(removeClient);
                }
            } finally {
                lock.unlock();
            }
        }

    }


    /**
     * @author chenglin.wu
     * @description: 创建线程池的factory 用于更改线程名
     * @title: KeepAliveThreadFactory
     * @projectName why-ftp
     * @date 2023/5/15
     * @company sjcnh-ctu
     */
    static class KeepAliveThreadFactory implements ThreadFactory {

        private final AtomicInteger atomicInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            String name = "Ftp Client KeepAlive" + atomicInteger.getAndIncrement();
            return new Thread(null, runnable, name, 0);
        }
    }
}

