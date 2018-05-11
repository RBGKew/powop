package org.powo.model.convert;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.convert.converter.Converter;

public class StringToDateTimeConverter implements Converter<String, DateTime> {

	@Override
	public DateTime convert(String string) {
		return ISODateTimeFormat.dateTime().parseDateTime(string);
	}

}
