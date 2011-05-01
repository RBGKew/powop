package org.emonocot.job.scratchpads.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class UnixTimeConverterTest {
    /**
     *
     */
    private static final long TEST_DATE = 1300970978000L;

    /**
     *
     */
    private UnixTimeConverter converter = new UnixTimeConverter();

    /**
     *
     */
    @Test
    public final void testNullConversion() {
        assertNull("Converter should be null safe", converter.convert(null));
    }

    /**
     *
     */
    @Test
    public final void testStringConversion() {
        assertEquals("Converter should convert to the correct instant",
                     converter.convert("1300970978"),
                     new DateTime(UnixTimeConverterTest.TEST_DATE));
    }
}
