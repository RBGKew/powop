package org.emonocot.model.marshall.xml;

import static org.junit.Assert.fail;
import java.text.ParseException;
import java.util.Locale;

import org.emonocot.model.marshall.xml.DateTimeConverter;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.springframework.format.datetime.joda.DateTimeParser;

/**
 * In response to http://build.e-monocot.org/bugzilla/show_bug.cgi?id=67.
 * @author ben
 *
 */
public class DateTimeConverterTest {


    /**
     *
     */
    private DateTimeParser dateTimeParser
        = new DateTimeParser(
                DateTimeFormat.forPattern(
                        DateTimeConverter.UTC_DATETIME_PATTERN));

    /**
     *
     */
    private static String TEST_DATE = "1970-01-15T21:56:09Z";

    /**
     *
     */
    @Test
    public void testParseDateTimeWithHoursAfterTwelve() {
        try {
            dateTimeParser.parse(TEST_DATE, Locale.ENGLISH);
        } catch (ParseException e) {
            fail("Should be able to parse " + TEST_DATE + " successfully");
        }
    }
}
