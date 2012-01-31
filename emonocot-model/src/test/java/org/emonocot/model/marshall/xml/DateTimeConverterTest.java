package org.emonocot.model.marshall.xml;

import static org.junit.Assert.fail;
import java.text.ParseException;
import java.util.Locale;

import org.easymock.EasyMock;
import org.emonocot.model.marshall.xml.DateTimeConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.format.datetime.joda.DateTimeParser;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

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
    private HierarchicalStreamWriter writer = null;

    /**
     *
     */
    private DateTimeConverter converter = new DateTimeConverter();

    /**
     *
     */
    @Before
    public final void setUp() {
        writer = EasyMock.createMock(HierarchicalStreamWriter.class);
    }

    /**
     *
     */
    @Test
    public final void testParseDateTimeWithHoursAfterTwelve() {
        try {
            dateTimeParser.parse("1970-01-15T21:56:09Z", Locale.ENGLISH);
        } catch (ParseException e) {
            fail("Should be able to parse date successfully");
        }
    }

    /**
     *
     */
    @Test
    public final void testMarshal() {
        DateTime value = new DateTime(2012,1,30,0,0,0, 0);
        writer.setValue(EasyMock.eq("2012-01-30T00:00:00Z"));
        EasyMock.replay(writer);

        converter.marshal(value, writer, null);

        EasyMock.verify(writer);
    }
}
