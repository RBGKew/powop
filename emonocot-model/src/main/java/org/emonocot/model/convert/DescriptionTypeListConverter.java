package org.emonocot.model.convert;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.constants.DescriptionType;
import org.springframework.core.convert.converter.Converter;

public class DescriptionTypeListConverter implements Converter<String, List<DescriptionType>> {

	@Override
	public List<DescriptionType> convert(String source) {
		source = source.trim();
		List<DescriptionType> result = new ArrayList<>();
		if(source != null && !source.isEmpty()) {
			for(String type : source.split("\\|")) {
				result.add(DescriptionType.fromString(type.trim()));
			}
		}

		return result;
	}
}
