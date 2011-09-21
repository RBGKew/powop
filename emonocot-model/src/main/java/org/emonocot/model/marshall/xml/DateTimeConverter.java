package org.emonocot.model.marshall.xml;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author ben
 *
 */
public class DateTimeConverter implements Converter {
    /**
     * Dates and times are uniformly encoded using ISO8601 and are expressed in
     * UTC throughout the protocol. When time is included, the special UTC
     * designator ("Z") must be used. UTC is implied for dates although no
     * timezone designator is specified. For example, 1957-03-20T20:30:00Z is
     * UTC 8:30:00 PM on March 20th 1957. UTCdatetime is used in both protocol
     * requests and protocol replies, in the way described in the following
     * sections.
     */
    public static final String UTC_DATETIME_PATTERN
        = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     *
     */
    private static final DateTimeFormatter PRINTER
        = ISODateTimeFormat.dateTimeNoMillis();
    /**
     *
     */
    private static final DateTimeFormatter PARSER
        =  ISODateTimeFormat.dateTimeParser();

    public final boolean canConvert(final Class clazz) {
       if (clazz != null && clazz.equals(DateTime.class)) {
           return true;
       }
       return false;
    }

    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {

        DateTime dateTime = (DateTime) value;
        writer.setValue(DateTimeConverter.PRINTER.print(
                dateTime.toDateTime(DateTimeZone.UTC)));
    }

    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        String value = reader.getValue();

        return DateTimeConverter.PARSER.parseDateTime(value);
    }

}
