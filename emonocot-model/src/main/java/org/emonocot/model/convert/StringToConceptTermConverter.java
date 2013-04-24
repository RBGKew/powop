package org.emonocot.model.convert;

import org.emonocot.api.job.TermFactory;
import org.gbif.dwc.terms.ConceptTerm;
import org.springframework.core.convert.converter.Converter;

public class StringToConceptTermConverter implements Converter<String, ConceptTerm> {

	private TermFactory termFactory = new TermFactory();
	
	@Override
	public ConceptTerm convert(String source) {
		if (source == null) {
			return null;
		}
		source = source.trim();
		if (source.isEmpty()) {
			return null;
		}
		ConceptTerm conceptTerm = termFactory.findTerm(source);

		return conceptTerm;
	}

}
