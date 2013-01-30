package org.emonocot.portal.controller.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.emonocot.portal.validation.FieldMatch;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@FieldMatch(first = "password", second = "repeatPassword", message = "The password fields must match")
public class ResetForm {
	
	@NotEmpty
	@Email
	private String username;
	
	@NotNull
	@Size(min=8, max=25)
	private String password;
	
	@NotNull
	@Size(min=8, max=25)
	private String repeatPassword;
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the repeatPassword
	 */
	public String getRepeatPassword() {
		return repeatPassword;
	}

	/**
	 * @param repeatPassword the repeatPassword to set
	 */
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

}
