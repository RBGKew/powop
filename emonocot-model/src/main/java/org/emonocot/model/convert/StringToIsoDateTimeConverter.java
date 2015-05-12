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
package org.emonocot.model.convert;

import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;

public class StringToIsoDateTimeConverter implements Converter<String, DateTime> {
	
	protected Parser<DateTime> dateTimeParser = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

	@Override
	public DateTime convert(String source) {
		if (source == null || source.trim().isEmpty()) {
            return null;
        } else {
        	try {
				return dateTimeParser.parse(source, null);
			} catch (ParseException pe) {
				throw new IllegalArgumentException("Could not convert " + source + " to an ISO date time",pe);
			}
        }
	}

}
