package org.emonocot.portal.controller.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class RecoveryForm {
	
	@NotEmpty
	@Email
	private String username;
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
