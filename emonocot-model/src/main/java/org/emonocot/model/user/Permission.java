package org.emonocot.model.user;

import org.springframework.security.core.GrantedAuthority;


public enum Permission implements GrantedAuthority {
	CREATE_TAXON,
	DELETE_TAXON,
	CREATE_IMAGE,
	DELETE_IMAGE;

	public String getAuthority() {
		return this.name();
	}

}
