package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.job.TermFactory;
import org.gbif.dwc.terms.ConceptTerm;


public class ConceptTermDeserializer extends JsonDeserializer<ConceptTerm> {
	
	private TermFactory termFactory = new TermFactory();

	@Override
	public ConceptTerm deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		String value = jsonParser.getText();
        return termFactory.findTerm(value);
	}

}
