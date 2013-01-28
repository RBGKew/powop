package org.emonocot.model.convert;

import org.joda.time.ReadableInstant;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Printer;
import org.springframework.format.datetime.joda.ReadableInstantPrinter;

public class IsoDateTimeToStringConverter implements
		Converter<ReadableInstant, String> {
	
	Printer<ReadableInstant> printer = new ReadableInstantPrinter(ISODateTimeFormat.dateTime());

	@Override
	public String convert(ReadableInstant source) {
		if(source == null) {
			return null;
		} else {
		    return printer.print(source, null);
		}
	}

}
