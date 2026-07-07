package com.salesreport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Dedicated thread pool for report generation workflows.
 * Isolates long-running async work from the servlet container's request threads.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    public static final String WORKFLOW_EXECUTOR = "workflowExecutor";

    @Bean(name = WORKFLOW_EXECUTOR)
    public Executor workflowExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("workflow-");
        executor.initialize();
        return executor;
    }
}
