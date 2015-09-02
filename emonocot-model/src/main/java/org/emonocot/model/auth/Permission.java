/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.model.auth;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author ben
 *
 */
public enum Permission implements GrantedAuthority {
	PERMISSION_CREATE_TAXON,
	PERMISSION_DELETE_TAXON,
	PERMISSION_CREATE_IMAGE,
	PERMISSION_DELETE_IMAGE,
	PERMISSION_CREATE_REFERENCE,
	PERMISSION_DELETE_REFERENCE,
	PERMISSION_CREATE_USER,
	PERMISSION_DELETE_USER,
	PERMISSION_CREATE_GROUP,
	PERMISSION_DELETE_GROUP,
	PERMISSION_VIEW_SOURCE,
	PERMISSION_CREATE_SOURCE,
	PERMISSION_DELETE_SOURCE,
	PERMISSION_CREATE_JOBEXECUTION,
	PERMISSION_DELETE_JOBEXECUTION,
	PERMISSION_CREATE_JOBINSTANCE,
	PERMISSION_DELETE_JOBINSTANCE,
	PERMISSION_CREATE_ANNOTATION,
	PERMISSION_DELETE_ANNOTATION,
	PERMISSION_ADMINISTRATE,
	PERMISSION_WRITE_GROUP,
	PERMISSION_DELETE_COMMENT,
	PERMISSION_CREATE_KEY,
	PERMISSION_DELETE_KEY,
	PERMISSION_CREATE_PHYLOGENY,
	PERMISSION_DELETE_PHYLOGENY,
	PERMISSION_CREATE_PLACE,
	PERMISSION_DELETE_PLACE,
	PERMISSION_CREATE_RESOURCE,
	PERMISSION_DELETE_RESOURCE,
	PERMISSION_CREATE_TERM,
	PERMISSION_DELETE_TERM,
	PERMISSION_CREATE_OCCURRENCE,
	PERMISSION_DELETE_OCCURRENCE;

	/**
	 * @return the authority
	 */
	 public String getAuthority() {
		return this.name();
	}

}
