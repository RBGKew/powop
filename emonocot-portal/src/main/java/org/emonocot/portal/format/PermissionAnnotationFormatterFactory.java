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
package org.emonocot.portal.format;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.portal.format.annotation.PermissionFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class PermissionAnnotationFormatterFactory implements
AnnotationFormatterFactory<PermissionFormat> {

	/**
	 *
	 */
	private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

	static {
		FIELD_TYPES.add(Permission.class);
	}

	/**
	 * @return the field types supported by this annotation formatter factory
	 */
	public final Set<Class<?>> getFieldTypes() {
		return FIELD_TYPES;
	}

	/**
	 * @param permissionFormat the annotation
	 * @param fieldType the field type
	 * @return a parser
	 */
	public final Parser<?> getParser(final PermissionFormat permissionFormat,
			final Class<?> fieldType) {
		return new PermissionFormatter();
	}

	/**
	 * @param permissionFormat the annotation
	 * @param fieldType the field type
	 * @return a printer
	 */
	public final Printer<?> getPrinter(final PermissionFormat permissionFormat,
			final Class<?> fieldType) {
		return new PermissionFormatter();
	}

}
