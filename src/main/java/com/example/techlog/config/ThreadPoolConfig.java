package com.example.techlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
