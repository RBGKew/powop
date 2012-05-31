/**
 * 
 */
package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author jk00kg
 *
 */
public class ShapeDeserializer extends JsonDeserializer<MultiPolygon> {

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public MultiPolygon deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		String wkt = jp.readValueAs(String.class);
		try {
			return (MultiPolygon) new WKTReader().read(wkt);
		} catch (ParseException e) {
			throw new JsonGenerationException("Unable to generate Multipolygon from " + wkt, e);
		}
	}

}
