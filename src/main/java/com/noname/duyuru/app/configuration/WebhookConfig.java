package com.noname.duyuru.app.configuration;

import com.noname.duyuru.app.service.telegram.PollingService;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebhookConfig {
	WebhookConfig(ConfigurationSet configurationSet, PollingService pollingService) throws InterruptedException {
		if (!configurationSet.getWebhookUrl().equals("")) {
			pollingService.deleteWebhook();
			pollingService.createWebhook();
		}else {
			pollingService.startPolling();
		}
	}
}
