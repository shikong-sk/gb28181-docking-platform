package cn.skcks.docking.gb28181.core.sip.executor;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@Order(1)
@EnableAsync(proxyTargetClass = true)
@Slf4j
public class DefaultSipExecutor {
    /**
     * cpu 核心数
     */
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    public static final int MAX_POOL_SIZE = CPU_NUM * 2;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 30;
    /**
     * 队列长度
     */
    public static final int TASK_NUM = 10000;
    /**
     * 线程名称(前缀)
     */
    public static final String THREAD_NAME_PREFIX = "sip-executor";

    public static final String EXECUTOR_BEAN_NAME = "sipTaskExecutor";

    private ThreadPoolTaskExecutor executor;

    @Bean(EXECUTOR_BEAN_NAME)
    public Executor sipTaskExecutor(){
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CPU_NUM);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(TASK_NUM);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    @PreDestroy
    public void destroy() {
        if (executor != null) {
            log.info("正在关闭SIP线程池...");
            executor.shutdown();
            try {
                if (!executor.getThreadPoolExecutor().awaitTermination(30, TimeUnit.SECONDS)) {
                    log.warn("SIP线程池未能在30秒内完成关闭，强制关闭");
                    executor.getThreadPoolExecutor().shutdownNow();
                } else {
                    log.info("SIP线程池已成功关闭");
                }
            } catch (InterruptedException e) {
                log.warn("SIP线程池关闭过程被中断，强制关闭");
                executor.getThreadPoolExecutor().shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}