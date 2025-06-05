package aytackydln.duyuru.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig{

	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
                .csrf(
                        c -> c
                                .ignoringRequestMatchers("/webhook/**", "/actuator/health")
                ).authorizeHttpRequests(
                        auth -> auth
                                .anyRequest()
                                .authenticated()
                )
				.httpBasic(
						Customizer.withDefaults()
				).build();
	}

	@Autowired
	public void configAuthentication(final AuthenticationManagerBuilder auth, final DataSource dataSource) throws Exception{
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery("select username,password, enabled from logins where username=?")
				.authoritiesByUsernameQuery("select username, authority from authorities where username=?")
				.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}