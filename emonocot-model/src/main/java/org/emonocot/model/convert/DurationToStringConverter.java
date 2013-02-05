package org.emonocot.model.convert;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.core.convert.converter.Converter;

public class DurationToStringConverter implements Converter<Duration, String> {
	
	private PeriodFormatter formatter = ISOPeriodFormat.standard();

	@Override
	public String convert(Duration source) {
		if(source == null) {
			return null;
		} else {
		    return formatter.print(new Period(source));
		}
	}

}
