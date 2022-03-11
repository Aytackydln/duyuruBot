package aytackydln.duyuru.configuration;

import aytackydln.chattools.telegram.dto.models.CustomKeyboard;
import aytackydln.chattools.telegram.dto.models.KeyboardItem;
import aytackydln.duyuru.configuration.port.ConfigurationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
public class AppConfig{

	@Bean
	public ConfigurationSet configurationSet(ConfigurationPort configurationPort){
		final Optional<String> configurationType=configurationPort.find("configurationType");
		if(configurationType.isPresent()){
			switch(configurationType.get()) {
				case "performant":
					return new PerformantConfigurationSet(configurationPort);
				case "dynamic":
					return new DynamicConfigurationSet(configurationPort);
				default:
					LOGGER.warn("Unknown configuration type: {}", configurationType.get());
			}
		}
		LOGGER.info("no 'configurationType' specified. using 'performant' configuration type");
		return new PerformantConfigurationSet(configurationPort);
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
