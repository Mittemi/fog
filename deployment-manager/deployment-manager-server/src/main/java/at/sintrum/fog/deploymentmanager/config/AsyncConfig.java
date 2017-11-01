package at.sintrum.fog.deploymentmanager.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by Michael Mittermayr on 06.07.2017.
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    public static final String PUSH_IMAGE_TASK_EXECUTOR = "pushImageTaskExecutor";
    public static final String PULL_IMAGE_TASK_EXECUTOR = "pullImageTaskExecutor";

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("DM-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new FogAsyncUncaughtExceptionHandler();
    }

    @Bean(name = PUSH_IMAGE_TASK_EXECUTOR)
    public TaskExecutor pushImageTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(15);
        return threadPoolTaskExecutor;
    }

    @Bean(name = PULL_IMAGE_TASK_EXECUTOR)
    public TaskExecutor pullImageTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(15);
        return threadPoolTaskExecutor;
    }
}
