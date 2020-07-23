package com.zzq.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sherlock
 */
@Configuration
public class ExecutorConfig {
    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutor() {
        return new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), r -> {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.NORM_PRIORITY);
            thread.setContextClassLoader(SpringApplication.class.getClassLoader());
            return thread;
        });
    }
}