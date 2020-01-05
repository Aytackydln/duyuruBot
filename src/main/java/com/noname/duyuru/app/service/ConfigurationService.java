package com.noname.duyuru.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.noname.duyuru.app.jpa.models.Configuration;
import com.noname.duyuru.app.jpa.repositories.ConfigurationRepository;

@Service
public class ConfigurationService {
	private static final Logger LOGGER = LogManager.getLogger(ConfigurationService.class);

	private final ConfigurationRepository configurationRepository;

	public ConfigurationService( final ConfigurationRepository configurationRepository){
		this.configurationRepository=configurationRepository;
	}

	public void set(String property, String value){
		final Configuration newConfiguration = new Configuration();
		newConfiguration.setProperty(property);
		newConfiguration.setValue(value);
		LOGGER.info("Setting "+property+" to: "+value);
		configurationRepository.save(newConfiguration);
	}
}
