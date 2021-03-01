package org.powo.harvest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${harvester.username}")
	private String username;

	@Value("${harvester.password}")
	private String password;

	@Autowired
	private RestAuthenticationEntryPoint entryPoint;


	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser(username)
		.password("{noop}" + password)
		.roles("USER", "PERMISSION_ADMINISTRATE");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(entryPoint)
		.and().authorizeRequests()
		.antMatchers(HttpMethod.POST, "/api/1/**").authenticated()
		.antMatchers(HttpMethod.DELETE, "/api/1/**").authenticated()
		.and().httpBasic()
		.and().logout();
	}
	
}