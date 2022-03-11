package aytackydln.duyuru.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsynchronousConfig {

    @Bean(name = "taskExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor scheduledExecutorService = new ThreadPoolTaskExecutor();
        scheduledExecutorService.setCorePoolSize(3);
        scheduledExecutorService.setMaxPoolSize(8);
        scheduledExecutorService.setKeepAliveSeconds(15);
        scheduledExecutorService.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        scheduledExecutorService.setWaitForTasksToCompleteOnShutdown(true);

        return scheduledExecutorService;
    }
}
