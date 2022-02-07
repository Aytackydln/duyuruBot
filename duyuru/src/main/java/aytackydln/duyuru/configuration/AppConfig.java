package aytackydln.duyuru.configuration;

import aytackydln.duyuru.jpa.models.ConfigurationEntity;
import aytackydln.duyuru.jpa.repository.ConfigurationRepository;
import aytackydln.chattools.telegram.dto.models.CustomKeyboard;
import aytackydln.chattools.telegram.dto.models.KeyboardItem;
import aytackydln.duyuru.setting.ConfigurationSet;
import aytackydln.duyuru.setting.DynamicConfigurationSet;
import aytackydln.duyuru.setting.PerformantConfigurationSet;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@Log4j2
public class AppConfig{

	@Bean
	public ConfigurationSet configurationSet(final ConfigurationRepository configurationRepository){
		final Optional<ConfigurationEntity> configurationType=configurationRepository.findById("configurationType");
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
