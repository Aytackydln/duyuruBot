package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.Configuration;
import com.noname.duyuru.app.jpa.repositories.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConfigurationService {
	private final ConfigurationRepository configurationRepository;

	public void set(String property, String value) {
        final Configuration newConfiguration = new Configuration();
        newConfiguration.setProperty(property);
        newConfiguration.setValue(value);
        LOGGER.info("Setting {} to: {}", property, value);
        configurationRepository.save(newConfiguration);
    }
}
