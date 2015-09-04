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
package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.emonocot.api.Service;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.Principal;
import org.emonocot.model.auth.User;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 * @author ben
 *
 */
public class PrincipalDeserializer extends
JsonDeserializer<Principal> {

	/**
	 *
	 */
	protected Service<User> userService;

	/**
	 *
	 */
	protected Service<Group> groupService;

	/**
	 *
	 */
	public PrincipalDeserializer() {
	}

	/**
	 *
	 * @param newGroupService
	 *            Set the group service
	 */
	public final void setGroupService(final Service<Group> newGroupService) {
		this.groupService = newGroupService;
	}

	/**
	 *
	 * @param newUserService
	 *            Set the user service
	 */
	public final void setUserService(final Service<User> newUserService) {
		this.userService = newUserService;
	}

	@Override
	public Principal deserialize(final JsonParser jsonParser,
			final DeserializationContext deserializationContext)
					throws IOException {
		String identifier = jsonParser.getText();
		/**
		 * Hack for now should allow client side to
		 * set "return lazy initialized proxy objs"
		 */
		if (userService != null && groupService != null) {
			Principal principal = groupService.find(identifier);
			if (principal == null) {
				return userService.find(identifier);
			} else {
				return principal;
			}
		} else {
			return null;
		}
	}
}
