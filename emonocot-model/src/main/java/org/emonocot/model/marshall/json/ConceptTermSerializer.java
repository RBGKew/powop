package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.gbif.dwc.terms.ConceptTerm;

public class ConceptTermSerializer extends JsonSerializer<ConceptTerm> {

	@Override
	public void serialize(ConceptTerm value, JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jsonGenerator.writeString(value.qualifiedNormalisedName());
	}

}
