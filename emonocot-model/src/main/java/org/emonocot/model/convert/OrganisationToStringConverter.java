package org.emonocot.model.convert;

import org.emonocot.model.registry.Organisation;
import org.springframework.core.convert.converter.Converter;

public class OrganisationToStringConverter implements Converter<Organisation, String> {

	@Override
	public String convert(Organisation source) {
		if(source == null) {
			return null;
		}
		return source.getIdentifier();
	}

}
