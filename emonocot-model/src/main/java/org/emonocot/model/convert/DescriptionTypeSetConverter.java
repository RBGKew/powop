package org.emonocot.model.convert;

import java.util.SortedSet;
import java.util.TreeSet;

import org.emonocot.model.constants.DescriptionType;
import org.springframework.core.convert.converter.Converter;

public class DescriptionTypeSetConverter implements Converter<String, SortedSet<DescriptionType>> {
	@Override
	public SortedSet<DescriptionType> convert(String source) {
		source = source.trim();
		SortedSet<DescriptionType> result = new TreeSet<>();
		if(source != null && !source.isEmpty()) {
			for(String type : source.split("\\|")) {
				result.add(DescriptionType.fromString(type.trim()));
			}
		}

		return result;
	}
}
