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

import org.powo.model.Taxon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TaxonSerializer extends JsonSerializer<Taxon> {
	@Override
	public final void serialize(final Taxon taxon, final JsonGenerator json, final SerializerProvider serializer)
			throws IOException {
		json.writeStartObject();
		json.writeStringField("fqId", taxon.getIdentifier());
		json.writeStringField("name", taxon.getScientificName());
		json.writeStringField("author", taxon.getScientificNameAuthorship());
		json.writeStringField("rank", taxon.getTaxonRank().toString());
		json.writeStringField("taxonomicStatus", taxon.getTaxonomicStatus().toString());
		json.writeEndObject();
	}
}
