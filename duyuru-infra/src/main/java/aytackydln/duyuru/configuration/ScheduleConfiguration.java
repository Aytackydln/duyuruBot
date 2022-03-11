package aytackydln.duyuru.configuration;

import aytackydln.duyuru.announcement.AnnouncementFacade;
import aytackydln.duyuru.topic.TopicUpdater;
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
	private final AnnouncementFacade announcementService;
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
			announcementService.checkNewAnnouncements();
		}
	}

	@Scheduled(cron = "0 0 0/3 * * ?")
	public final void dailyUpdate() {
		topicUpdater.updateTopics();
		if (configurationSet.isCleaningEnabled())
			announcementService.clearAnnouncements();
	}
}
