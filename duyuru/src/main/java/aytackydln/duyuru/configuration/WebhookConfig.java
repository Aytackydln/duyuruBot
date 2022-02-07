package aytackydln.duyuru.configuration;

import aytackydln.duyuru.service.telegram.PollingService;
import aytackydln.duyuru.setting.ConfigurationSet;
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
