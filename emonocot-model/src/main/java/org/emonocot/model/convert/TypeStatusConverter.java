package org.emonocot.model.convert;

import org.gbif.ecat.voc.TypeStatus;
import org.springframework.core.convert.converter.Converter;

public class TypeStatusConverter implements Converter<String, TypeStatus> {

	@Override
	public TypeStatus convert(String source) {
		if(source == null || source.isEmpty()) {
			return null;
		} else {
			return TypeStatus.valueOf(source.toLowerCase());
		}
	}

}
