package org.emonocot.model.convert;

import org.emonocot.model.constants.ReferenceType;
import org.springframework.core.convert.converter.Converter;

public class ReferenceTypeConverter implements Converter<String, ReferenceType> {

	@Override
	public ReferenceType convert(String source) {
		if(source == null || source.isEmpty()) {
			return null;
		} else {
			return ReferenceType.valueOf(source);
		}
	}

}
