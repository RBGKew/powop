package org.emonocot.job.scratchpads.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.junit.Test;


public class UnixTimeConverterTest {
	
	private UnixTimeConverter converter = new UnixTimeConverter();
	
	@Test
	public void testNullConversion() {
		assertNull("Converter should be null safe",converter.convert(null));
	}
	
	@Test
	public void testStringConversion() {
		assertEquals("Converter should convert to the correct instant",converter.convert("1300970978"),new DateTime(1300970978000l));
	}

}
