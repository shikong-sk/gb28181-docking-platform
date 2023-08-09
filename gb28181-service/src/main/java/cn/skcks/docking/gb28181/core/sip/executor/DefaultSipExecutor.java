package cn.skcks.docking.gb28181.core.sip.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@Order(1)
@EnableAsync(proxyTargetClass = true)
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


    @Bean(EXECUTOR_BEAN_NAME)
    public Executor sipTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
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
}
