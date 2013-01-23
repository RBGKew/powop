package org.emonocot.model.convert;

import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;

public class StringToIsoDateTimeConverter implements Converter<String, DateTime> {
	
	protected Parser<DateTime> dateTimeParser = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

	@Override
	public DateTime convert(String source) {
		if (source == null || source.trim().isEmpty()) {
            return null;
        } else {
        	try {
				return dateTimeParser.parse(source, null);
			} catch (ParseException pe) {
				throw new IllegalArgumentException("Could not convert " + source + " to an ISO date time",pe);
			}
        }
	}

}
