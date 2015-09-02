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
package org.emonocot.api;

import java.util.List;

import org.emonocot.model.SecuredObject;
import org.emonocot.model.auth.Group;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public interface GroupService extends SearchableService<Group> {

	/**
	 *
	 * @param object the object to add the permission to
	 * @param recipient the recipient recieving the permission
	 * @param permission the permission itself
	 * @param clazz the class of object
	 */
	void addPermission(SecuredObject object, String recipient,
			Permission permission, Class<? extends SecuredObject> clazz);
	/**
	 *
	 * @param object the object to remove the permission from
	 * @param recipient the recipient with the permission
	 * @param permission the permission itself
	 * @param clazz the class of object
	 */
	void deletePermission(SecuredObject object, String recipient,
			Permission permission, Class<? extends SecuredObject> clazz);

	/**
	 *
	 * @param recipient the recipent of the permissions
	 * @return a list of Access Control Entries
	 */
	List<Object[]> listAces(String recipient);

}
