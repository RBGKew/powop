package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.gbif.dwc.terms.ConceptTerm;

public class ConceptTermSerializer extends JsonSerializer<ConceptTerm> {

	@Override
	public void serialize(ConceptTerm value, JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jsonGenerator.writeString(value.qualifiedNormalisedName());
	}

}
