package org.emonocot.checklist.test;

import static org.junit.Assert.fail;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class DateTimeFormatterTest {

    /**
     *
     */
    @Test
    public final void testDateTimeFormatter() {
        DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();
        try {
            dateTimeFormatter.parseDateTime("2010-06-17T10:45:45.067Z");
        } catch (Exception e) {
            fail("No " + e.getClass().getName() + " expected here");
        }

    }
}
