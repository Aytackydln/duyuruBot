package aytackydln.duyuru.configuration;

import aytackydln.duyuru.service.AnnouncementService;
import aytackydln.duyuru.service.SubscriptionService;
import aytackydln.duyuru.service.TopicUpdater;
import aytackydln.duyuru.setting.ConfigurationSet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfiguration {
	private final ConfigurationSet configurationSet;
	private final AnnouncementService announcementService;
	private final SubscriptionService subscriptionService;
	private final TopicUpdater topicUpdater;

	@Bean(name="taskScheduler", destroyMethod="shutdown")
	public ThreadPoolTaskScheduler taskExecutor(){
		final ThreadPoolTaskScheduler scheduledExecutorService=new ThreadPoolTaskScheduler();
		scheduledExecutorService.setPoolSize(2);
		scheduledExecutorService.setWaitForTasksToCompleteOnShutdown(true);

		return scheduledExecutorService;
	}

	@Scheduled(cron = "0 0/5 * * * ?")
	public final void checkAnnouncements() {
		if (configurationSet.isAnnouncementCheckEnabled()) {
			subscriptionService.checkNewAnnouncements();
		}
	}

	@Scheduled(cron = "0 0 0/3 * * ?")
	public final void dailyUpdate() {
		topicUpdater.updateTopics();
		if (configurationSet.isCleaningEnabled())
			announcementService.clearAnnouncements();
	}
}
