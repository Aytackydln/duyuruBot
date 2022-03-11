package aytackydln.duyuru;

import aytackydln.duyuru.common.semantic.DomainComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(
		scanBasePackages = {"aytackydln.duyuru"}
)
@ComponentScan(
		includeFilters = {
				@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {DomainComponent.class})
		}
)
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
