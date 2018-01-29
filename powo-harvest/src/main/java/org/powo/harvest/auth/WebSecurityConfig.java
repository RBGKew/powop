package org.powo.harvest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${harvester.username}")
	private String username;

	@Value("${harvester.password}")
	private String password;

	@Autowired
	private RestAuthenticationEntryPoint entryPoint;

	@Autowired
	private RestSavedRequestAwareAuthenticationSuccessHandler successHandler;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser(username)
		.password(password)
		.roles("USER", "PERMISSION_ADMINISTRATE");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(entryPoint)
		.and().authorizeRequests()
		.antMatchers("/api/1/**").authenticated()
		.and().formLogin()
		.successHandler(successHandler)
		.failureHandler(new SimpleUrlAuthenticationFailureHandler())
		.and().logout();
	}

	@Bean
	public RestSavedRequestAwareAuthenticationSuccessHandler apiSuccessHandler() {
		return new RestSavedRequestAwareAuthenticationSuccessHandler();
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler apiFailuerHandler() {
		return new SimpleUrlAuthenticationFailureHandler();
	}
}