package org.powo.model.convert;

import org.powo.model.constants.MediaType;
import org.springframework.core.convert.converter.Converter;

public class StringToMediaTypeConverter implements Converter<String, MediaType> {
	@Override
	public MediaType convert(String source) {
		return MediaType.fromString(source);
	}
}
