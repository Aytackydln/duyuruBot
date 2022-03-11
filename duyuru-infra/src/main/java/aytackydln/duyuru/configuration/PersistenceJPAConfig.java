package aytackydln.duyuru.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@Configuration
@EnableJpaRepositories("aytackydln.duyuru.jpa.repository")
@Log4j2
public class PersistenceJPAConfig{

	@ExceptionHandler(ConnectException.class)
	public void connectionExceptionHandle(){
		LOGGER.error("Could not connect to database");
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
}