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
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.emonocot.api.Service;
import org.emonocot.model.Base;

/**
 *
 * @author ben
 *
 */
public class AnnotatableObjectDeserializer extends JsonDeserializer<Base> {

	/**
	 *
	 */
	private Set<Service<? extends Base>> services = new HashSet<Service<? extends Base>>();

	/**
	 *
	 * @param service Set the service
	 */
	public final void addService(final Service<? extends Base> service) {
		if (service != null) {
			this.services.add(service);
		}
	}

	@Override
	public final Base deserialize(final JsonParser jsonParser,
			final DeserializationContext deserializationContext)
					throws IOException {
		String identifier = jsonParser.getText();
		if (identifier == null) {
			return null;
		}
		for (Service<? extends Base> service : services) {
			Base base = service.find(identifier);
			if (base != null) {
				return base;
			}
		}
		return null;
	}
}
