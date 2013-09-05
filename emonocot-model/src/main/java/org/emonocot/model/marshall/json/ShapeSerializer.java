package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * @author jk00kg
 *
 */
public class ShapeSerializer extends JsonSerializer<Geometry> {

	/* (non-Javadocom.fasterxml.jackson.databind..jackson.databind.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackscom.fasterxml.jackson.databind.terxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(Geometry value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		WKTWriter shapeWriter = new WKTWriter();
		String wkt = shapeWriter.write(value);
		jgen.writeString(wkt);
	}

}
