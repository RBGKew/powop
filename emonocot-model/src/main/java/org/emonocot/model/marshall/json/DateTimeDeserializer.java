package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateTimeDeserializer extends JsonDeserializer<DateTime> {

	   private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

	    @Override
	    public final DateTime deserialize(final JsonParser jsonParser,
	            final DeserializationContext deserializationContext)
	            throws IOException {
	        String value = jsonParser.getText();
	        return dateTimeFormatter.parseDateTime(value);
	    }

}
