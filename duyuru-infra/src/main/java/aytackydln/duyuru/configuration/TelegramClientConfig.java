package aytackydln.duyuru.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAsync
@Log4j2
public class TelegramClientConfig {
	public static final int MAX_MESSAGE_QUEUE = 5;
	public static final String LIMITED_COMMAND_SENDER = "telegramLimitedCommandSender";
	public static int maxCommandPerSecond = 30;

	@Bean
	RestTemplate telegramClient(RestTemplateBuilder builder, ConfigurationSet configurationSet) {
		return builder.rootUri("https://api.telegram.org/bot" + configurationSet.getBotToken()).build();
	}

	@Bean(name = LIMITED_COMMAND_SENDER, destroyMethod = "shutdown")
	public ThreadPoolTaskExecutor telegramLimitedCommandSender() {
		final ThreadPoolTaskExecutor limitedExecutor = new ThreadPoolTaskExecutor();
		limitedExecutor.setCorePoolSize(1);
		limitedExecutor.setMaxPoolSize(1);
		limitedExecutor.setWaitForTasksToCompleteOnShutdown(true);

		limitedExecutor.setQueueCapacity(MAX_MESSAGE_QUEUE);

		//block caller when queue is full
		limitedExecutor.setRejectedExecutionHandler(((runnable, executor) -> {
			try {
				//TODO exeqtor içinde queue full iken çalışırsa?
				executor.getQueue().put(runnable);
			} catch (InterruptedException e) {
				LOGGER.error("limited command could not be added to queue", e);
			}
		}));

		//This decorator limits the rate of command run
		limitedExecutor.setTaskDecorator((runnable -> () -> {
			final long startTime = System.currentTimeMillis();
			runnable.run();
			final long timeElapsed = System.currentTimeMillis() - startTime;
			try {
				long waitMill = Math.max(1000 / maxCommandPerSecond - timeElapsed, 0);
				Thread.sleep(waitMill);
				LOGGER.debug("waited for {} milliseconds, elapsed {}", waitMill, timeElapsed);
			} catch (InterruptedException ignored) {
			}
		}));

		return limitedExecutor;
	}
}
