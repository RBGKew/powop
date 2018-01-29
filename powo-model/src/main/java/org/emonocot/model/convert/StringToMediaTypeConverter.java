package org.emonocot.model.convert;

import org.emonocot.model.constants.MediaType;
import org.springframework.core.convert.converter.Converter;

public class StringToMediaTypeConverter implements Converter<String, MediaType> {
	@Override
	public MediaType convert(String source) {
		return MediaType.fromString(source);
	}
}
