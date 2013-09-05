/**
 * 
 */
package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author jk00kg
 *
 */
public class ShapeDeserializer extends JsonDeserializer<Geometry> {

	/* (non-Javadocom.fasterxml.jackson.databind..jackson.databind.JsonDeserializer#deserialize(org.codehaus.jacom.fasterxml.jackson.databind.terxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public Geometry deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		String wkt = jp.readValueAs(String.class);
		try {
			return new WKTReader().read(wkt);
		} catch (ParseException e) {
			throw new JsonGenerationException("Unable to generate Multipolygon from " + wkt, e);
		}
	}

}
