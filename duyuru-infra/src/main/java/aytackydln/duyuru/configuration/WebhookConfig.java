package aytackydln.duyuru.configuration;

import aytackydln.duyuru.adapter.telegram.PollingService;
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
