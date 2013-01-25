package org.emonocot.portal.controller;

import org.hibernate.validator.constraints.NotEmpty;

public class RecoveryForm {
	
	@NotEmpty
	private String username;
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
