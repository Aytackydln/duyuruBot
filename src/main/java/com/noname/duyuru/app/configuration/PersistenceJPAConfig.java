package com.noname.duyuru.app.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@Configuration
@EnableJpaRepositories("com.noname.duyuru.app.jpa.repositories")
public class PersistenceJPAConfig{
	private static final Logger LOGGER = LogManager.getLogger(PersistenceJPAConfig.class);

	@ExceptionHandler(ConnectException.class)
	public void connectionExceptionHandle(){
		LOGGER.error("Could not connect to database");
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
}