package org.emonocot.model.convert;

import org.emonocot.model.Reference;
import org.springframework.core.convert.converter.Converter;

public class ReferenceToStringConverter implements Converter<Reference, String> {

	@Override
	public String convert(Reference reference) {
		return reference.getIdentifier();
	}

}
