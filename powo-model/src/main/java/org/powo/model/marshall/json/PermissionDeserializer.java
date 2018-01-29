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
package org.powo.model.marshall.json;

import java.io.IOException;

import org.powo.model.convert.StringToPermissionConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.acls.model.Permission;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 * @author ben
 *
 */
public class PermissionDeserializer extends JsonDeserializer<Permission> {

	/**
	 *
	 */
	private Converter<String, Permission> converter = new StringToPermissionConverter();

	@Override
	public final Permission deserialize(final JsonParser jsonParser,
			final DeserializationContext deserializationContext)
					throws IOException {
		String permission = jsonParser.getText();
		try {
			return converter.convert(permission);
		} catch (IllegalArgumentException iae) {
			throw new JsonParseException(iae.getMessage(),
					jsonParser.getCurrentLocation(), iae);
		}
	}

}
