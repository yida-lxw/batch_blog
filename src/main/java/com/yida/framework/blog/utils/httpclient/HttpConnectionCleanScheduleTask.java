package com.yida.framework.blog.utils.httpclient;

import com.yida.framework.blog.utils.httpclient.config.AbstractHttpClientConfigurable;
import com.yida.framework.blog.utils.httpclient.factory.HttpClientFactory;
import com.yida.framework.blog.utils.thread.CustomThreadFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 13:49
 * @Description Http空闲和过期Connection清理的定时任务
 */
public class HttpConnectionCleanScheduleTask extends AbstractHttpClientConfigurable {
    private final Thread thread;
    private final Runnable cleanTaskRunnable;
    private Logger log = LogManager.getLogger(CleanTask.class.getName());
    private ScheduledExecutorService scheduledExecutorService;

    private HttpConnectionCleanScheduleTask() {
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ThreadFactory threadFactory = new CustomThreadFactory("IdleConnectionEvictorThread_");
        this.cleanTaskRunnable = new CleanTask();
        this.thread = threadFactory.newThread(this.cleanTaskRunnable);

    }

    public static final HttpConnectionCleanScheduleTask getInstance() {
        return HttpConnectionCleanScheduleTask.SingletonHolder.INSTANCE;
    }

    /**
     * 定时清理任务启动
     */
    public void start() {
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        this.scheduledExecutorService.scheduleAtFixedRate(
                this.cleanTaskRunnable, 10,
                this.clientConfig.getHttpConnectionEvictorSleepTime(), TimeUnit.MILLISECONDS);
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    private static class SingletonHolder {
        private static final HttpConnectionCleanScheduleTask INSTANCE = new HttpConnectionCleanScheduleTask();
    }

    class CleanTask implements Runnable {
        @Override
        public void run() {
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                    HttpClientFactory.getInstance().getPoolingHttpClientConnectionManager();
            log.info("The cleanup task thread[{}] beginning performing...", thread.getName());
            try {
                if (!Thread.currentThread().isInterrupted()) {
                    poolingHttpClientConnectionManager.closeExpiredConnections();
                    long maxIdleTimeMs = clientConfig.getHttpConnectionIdleMaxTime();
                    if (maxIdleTimeMs > 0L) {
                        poolingHttpClientConnectionManager.closeIdleConnections(maxIdleTimeMs, TimeUnit.MILLISECONDS);
                    }
                }
            } catch (Exception e) {
                log.error("The cleanup task thread[{}] performs an exception during execution:\n{}.",
                        Thread.currentThread().getName(), e.getMessage());
            }
            log.info("The cleanup task thread[{}] have finished the current perform,wait for the next round.", thread.getName());
        }
    }
}
