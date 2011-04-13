package org.emonocot.job.scratchpads.convert;

import org.emonocot.model.description.Feature;
import org.springframework.core.convert.converter.Converter;

public class FeatureConverter implements Converter<String,Feature> {

	@Override
	public Feature convert(String value) {
		if(value == null) {
			return null;
		}
		return Feature.fromString(value);
	}

}
