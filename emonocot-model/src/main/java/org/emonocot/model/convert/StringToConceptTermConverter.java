package org.emonocot.model.convert;

import org.emonocot.api.job.TermFactory;
import org.gbif.dwc.terms.Term;
import org.springframework.core.convert.converter.Converter;

public class StringToConceptTermConverter implements Converter<String, Term> {

	private TermFactory termFactory = new TermFactory();
	
	@Override
	public Term convert(String source) {
		if (source == null) {
			return null;
		}
		source = source.trim();
		if (source.isEmpty()) {
			return null;
		}
		Term conceptTerm = termFactory.findTerm(source);

		return conceptTerm;
	}

}
