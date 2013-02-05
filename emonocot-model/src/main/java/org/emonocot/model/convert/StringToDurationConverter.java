package org.emonocot.model.convert;

import org.joda.time.Duration;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.core.convert.converter.Converter;

public class StringToDurationConverter implements Converter<String, Duration> {

	private PeriodFormatter formatter = ISOPeriodFormat.standard();
	
	@Override
	public Duration convert(String source) {
		if(source == null || source.isEmpty()) {
			return null;
		} else {
		    return new Duration(formatter.parsePeriod(source));
		}
	}

}
