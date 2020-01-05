package com.noname.duyuru.app.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityManagerFactory;

import java.net.ConnectException;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EnableJpaRepositories("com.noname.duyuru.app.jpa.repositories")
@EnableSpringDataWebSupport
public class PersistenceJPAConfig{
	private static final Logger LOGGER = LogManager.getLogger(PersistenceJPAConfig.class);

	@ExceptionHandler(ConnectException.class)
	public void connectionExceptionHandle(){
		LOGGER.error("Could not connect to database");
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
		final JpaTransactionManager transactionManager=new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
}