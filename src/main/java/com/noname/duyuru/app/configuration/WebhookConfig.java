package com.noname.duyuru.app.configuration;

import org.springframework.context.annotation.Configuration;

import com.noname.duyuru.app.service.PollingService;
import com.noname.duyuru.app.setting.ConfigurationSet;

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
