package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author ben
 *
 */
public class DateTimeDeserializer extends JsonDeserializer<DateTime> {

   /**
    *
    */
   private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

    @Override
    public final DateTime deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String value = jsonParser.getText();
        return dateTimeFormatter.parseDateTime(value);
    }

}
