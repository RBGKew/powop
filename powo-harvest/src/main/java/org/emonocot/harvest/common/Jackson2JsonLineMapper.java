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
package org.emonocot.harvest.common;

import java.util.Map;

import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public class Jackson2JsonLineMapper implements LineMapper<Map<String,Object>>{

	private MappingJsonFactory factory = new MappingJsonFactory();

	@Override
	public Map<String, Object> mapLine(String line, int lineNumber)
			throws Exception {
		Map<String, Object> result;
		try {
			JsonParser parser = factory.createParser(line);
			@SuppressWarnings("unchecked")
			Map<String, Object> token = parser.readValueAs(Map.class);
			result = token;
		}
		catch (Exception e) {
			throw new FlatFileParseException("Cannot parse line to JSON", e, line, lineNumber);
		}
		return result;
	}

}
