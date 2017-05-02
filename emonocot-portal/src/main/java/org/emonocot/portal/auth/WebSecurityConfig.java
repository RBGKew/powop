package org.emonocot.portal.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
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

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser(username)
		.password(password)
		.roles("USER", "PERMISSION_ADMINISTRATE");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/organisation/**", "/resource/**").authenticated()
		.anyRequest().permitAll()
		.and().formLogin()
		.and().csrf().disable();
	}
}