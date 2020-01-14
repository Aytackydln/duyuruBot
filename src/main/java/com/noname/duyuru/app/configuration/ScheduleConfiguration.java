package com.noname.duyuru.app.configuration;

import com.noname.duyuru.app.service.AnnouncementService;
import com.noname.duyuru.app.service.TopicUpdater;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class ScheduleConfiguration {

	private final ConfigurationSet configurationSet;
	private final AnnouncementService announcementService;
	private final TopicUpdater topicUpdater;

	public ScheduleConfiguration(ConfigurationSet configurationSet, AnnouncementService announcementService,
			TopicUpdater topicUpdater) {
		this.configurationSet = configurationSet;
		this.announcementService = announcementService;
		this.topicUpdater = topicUpdater;
	}

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

	@Scheduled(cron = "0 0 5 * * ?")
    public final void dailyUpdate() {
        topicUpdater.updateTopics();
        if (configurationSet.isCleaningEnabled())
            announcementService.clearAnnouncements();
    }
}
