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
package org.powo.model.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class StringToPermissionConverter implements
Converter<String, Permission> {

	/**
	 * @param permission Set the string value
	 * @return a permission
	 */
	public final Permission convert(final String permission) {
		if (permission == null) {
			return null;
		} else if (permission.equals("CREATE")) {
			return BasePermission.CREATE;
		} else if (permission.equals("READ")) {
			return BasePermission.READ;
		} else if (permission.equals("WRITE")) {
			return BasePermission.WRITE;
		} else if (permission.equals("DELETE")) {
			return BasePermission.DELETE;
		} else if (permission.equals("ADMINISTRATION")) {
			return BasePermission.ADMINISTRATION;
		} else {
			throw new IllegalArgumentException(permission
					+ " is not a valid value for a Permission");
		}
	}

}
