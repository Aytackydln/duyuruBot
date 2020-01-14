package com.noname.duyuru.app.configuration;

import com.noname.duyuru.app.jpa.repositories.ConfigurationRepository;
import com.noname.duyuru.app.json.models.CustomKeyboard;
import com.noname.duyuru.app.json.models.KeyboardItem;
import com.noname.duyuru.app.setting.ConfigurationSet;
import com.noname.duyuru.app.setting.DynamicConfigurationSet;
import com.noname.duyuru.app.setting.PerformantConfigurationSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
public class AppConfig{
	private static final Logger LOGGER = LogManager.getLogger(AppConfig.class);

	@Bean
	public ConfigurationSet configurationSet(final ConfigurationRepository configurationRepository){
		final Optional<com.noname.duyuru.app.jpa.models.Configuration> configurationType=configurationRepository.findById("configurationType");
		if(configurationType.isPresent()){
			switch(configurationType.get().getValue()) {
				case "performant":
					return new PerformantConfigurationSet(configurationRepository);
				case "dynamic":
					return new DynamicConfigurationSet(configurationRepository);
				default:
					LOGGER.warn("Unknown configuration type: {}", configurationType.get().getValue());
			}
		}
		LOGGER.info("no 'configurationType' specified. using 'performant' configuration type");
		return new PerformantConfigurationSet(configurationRepository);
	}

	@Bean
	public CustomKeyboard userKeyboard(){
		final CustomKeyboard customKeyboard=new CustomKeyboard();
		final List<KeyboardItem> secondRow=customKeyboard.addRow();
		secondRow.add(new KeyboardItem("/list"));
		secondRow.add(new KeyboardItem("/subscribe"));
		secondRow.add(new KeyboardItem("/unsubscribe"));

		return customKeyboard;
	}
}
