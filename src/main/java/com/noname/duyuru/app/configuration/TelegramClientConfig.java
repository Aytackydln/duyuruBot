package com.noname.duyuru.app.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.noname.duyuru.app.setting.ConfigurationSet;

@Configuration
public class TelegramClientConfig {
	@Bean
	RestTemplate telegramClient(RestTemplateBuilder builder, ConfigurationSet configurationSet){
		return builder.rootUri("https://api.telegram.org/bot"+configurationSet.getBotToken()).build();
	}
}
