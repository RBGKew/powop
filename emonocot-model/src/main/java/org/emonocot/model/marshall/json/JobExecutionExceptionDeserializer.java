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

import org.springframework.batch.core.JobExecutionException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 * @author ben
 *
 */
public class JobExecutionExceptionDeserializer extends
JsonDeserializer<JobExecutionException> {

	/**
	 * @param jsonParser Set the json parser
	 * @param deserializationContext Set the deserialization context
	 * @return a JobExecutionInfo object
	 * @throws IOException if there is a problem
	 */
	@Override
	public final JobExecutionException deserialize(final JsonParser jsonParser,
			final DeserializationContext deserializationContext)
					throws IOException {
		try {
			JsonToken jsonToken = jsonParser.nextToken();
			assert jsonParser.getCurrentName() == "errors";
			jsonToken = jsonParser.nextToken();
			assert jsonToken == JsonToken.START_OBJECT;
			jsonToken = jsonParser.nextToken();
			// error code
			jsonToken = jsonParser.nextToken();
			JobExecutionException jobExecutionException = new JobExecutionException(
					jsonParser.getText());
			jsonToken = jsonParser.nextToken();
			while (jsonToken != JsonToken.END_OBJECT) {
				jsonToken = jsonParser.nextToken();
			}
			return jobExecutionException;
		} catch (AssertionError ae) {
			throw new IllegalArgumentException("Cannot convert into JobExecutionException");
		}
	}

}
