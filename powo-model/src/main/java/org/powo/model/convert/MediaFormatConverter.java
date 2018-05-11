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

import org.powo.model.constants.MediaFormat;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class MediaFormatConverter implements
Converter<String, MediaFormat> {

	/**
	 * @param value the string to convert
	 * @return an ImageFormat object or null if the image format term is null
	 */
	public final MediaFormat convert(final String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		} else {
			if (value.equals("image/jpeg")) {
				return MediaFormat.jpg;
			} else if (value.equals("image/png")) {
				return MediaFormat.png;
			} else if (value.equals("image/gif")) {
				return MediaFormat.png;
			} else if (value.equals("application/xml")) {
				return MediaFormat.xml;
			} else if (value.equals("text/plain")) {
				return MediaFormat.txt;
			} else {
				return MediaFormat.valueOf(value);
			}
		}
	}

}
