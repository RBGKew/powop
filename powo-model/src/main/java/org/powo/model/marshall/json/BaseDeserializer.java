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

import org.powo.api.Service;
import org.powo.model.Base;
import org.powo.model.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class BaseDeserializer<T extends Base> extends JsonDeserializer<T> {

	private static Logger logger = LoggerFactory.getLogger(BaseDeserializer.class);

	protected Service<T> service;

	protected Class<T> type;

	public BaseDeserializer(final Class<T> newType) {
		type = newType;
	}

	public final void setService(final Service<T> newService) {
		this.service = newService;
	}

	@Override
	public T deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
		String identifier = jsonParser.getText();
		/**
		 * Hack for now should allow client side to
		 * set "return lazy initialized proxy objs"
		 */
		if (service != null) {
			logger.debug("service is not null, trying to return object");
			try {
				return service.load(identifier);
			} catch (NotFoundException e) {
				logger.warn(e.getMessage());
			}
		} else {
			logger.debug("service is null");
		}

		try {
			logger.debug("returning new object");
			T t = type.newInstance();
			t.setIdentifier(identifier);
			return t;
		} catch (InstantiationException ie) {
			throw new JsonParseException(jsonParser, ie.getMessage(), jsonParser.getCurrentLocation());
		} catch (IllegalAccessException iae) {
			throw new JsonParseException(jsonParser, iae.getMessage(), jsonParser.getCurrentLocation());
		}
	}
}
