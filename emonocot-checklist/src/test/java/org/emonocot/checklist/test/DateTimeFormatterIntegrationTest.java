package org.emonocot.checklist.test;

import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext-test.xml" })
public class DateTimeFormatterIntegrationTest {

    /**
     *
     */
    @Autowired
    private ConversionService conversionService;

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

    /**
     *
     */
    @Test
    public final void testConversionService() {

        try {
            conversionService.convert("17/06/10 10:45", DateTime.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                System.err.println(ste);
            }
            System.err.println(e.getCause().getMessage());
            for (StackTraceElement ste : e.getCause().getStackTrace()) {
                System.err.println(ste);
            }
            fail("No " + e.getClass().getName() + " expected here");
        }

    }

}
