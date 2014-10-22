package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.emonocot.api.job.TermFactory;
import org.gbif.dwc.terms.Term;


public class TermDeserializer extends JsonDeserializer<Term> {
	
	private TermFactory termFactory = new TermFactory();

	@Override
	public Term deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		String value = jsonParser.getText();
        return termFactory.findTerm(value);
	}

}
