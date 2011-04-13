package org.emonocot.job.scratchpads.convert;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;

/**
 * Converter which converts from 'Unix Time' - a moment in the datetime continuum specified as a number of milliseconds from 1970-01-01T00:00Z.
 * to the JodaTime DateTime object.
 * 
 * @author ben
 *
 */
public class UnixTimeConverter implements Converter<String, DateTime> {

	@Override
	public DateTime convert(String value) {
		if(value == null) {
			return null;
		}
		return new DateTime(Long.valueOf(value) * 1000l);
	}

}
