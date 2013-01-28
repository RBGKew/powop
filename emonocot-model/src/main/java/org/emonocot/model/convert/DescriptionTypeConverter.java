package org.emonocot.model.convert;

import org.emonocot.model.constants.DescriptionType;
import org.springframework.core.convert.converter.Converter;

public class DescriptionTypeConverter implements Converter<String, DescriptionType> {

	@Override
	public DescriptionType convert(String source) {
		if(source == null || source.isEmpty()) {
			return null;
		} else {
		    return DescriptionType.fromString(source);
		}
	}

}
