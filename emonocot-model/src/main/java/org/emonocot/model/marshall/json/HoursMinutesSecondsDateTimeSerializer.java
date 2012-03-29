package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author ben
 *
 */
public class HoursMinutesSecondsDateTimeSerializer extends
        JsonSerializer<DateTime> {
    /**
     *
     */
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat
            .forPattern("HH:mm:ss");

    @Override
    public final void serialize(final DateTime dateTime,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(dateTimeFormatter.print(dateTime));
    }

}
