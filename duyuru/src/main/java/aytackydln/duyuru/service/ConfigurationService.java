package aytackydln.duyuru.service;

import aytackydln.duyuru.jpa.models.ConfigurationEntity;
import aytackydln.duyuru.jpa.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConfigurationService {
	private final ConfigurationRepository configurationRepository;

	public void set(String property, String value) {
        var newConfiguration = new ConfigurationEntity();
        newConfiguration.setProperty(property);
        newConfiguration.setValue(value);
        LOGGER.info("Setting {} to: {}", property, value);
        configurationRepository.save(newConfiguration);
    }
}
