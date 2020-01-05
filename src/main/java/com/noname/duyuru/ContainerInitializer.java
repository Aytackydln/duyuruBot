package com.noname.duyuru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ContainerInitializer extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ContainerInitializer.class).properties("spring.config.name: duyuru");
	}

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "duyuru");
		SpringApplication.run(ContainerInitializer.class, args);
	}
}
