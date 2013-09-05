package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializer extends JsonSerializer<DateTime> {

	   private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

	    @Override
	    public final void serialize(final DateTime dateTime,
	            final JsonGenerator jsonGenerator,
	            final SerializerProvider serializerProvider) throws IOException {
	        jsonGenerator.writeString(dateTimeFormatter.print(dateTime));
	    }
}
