package org.emonocot.model.convert;

import org.emonocot.model.geography.Location;
import org.springframework.core.convert.converter.Converter;

public class LocationToStringConverter implements Converter<Location, String> {

	@Override
	public String convert(Location source) {
		if(source == null) {
			return null;
		} else {
		    return source.getPrefix() + ":" + source.getCode();
		}
	}

}
