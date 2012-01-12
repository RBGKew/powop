package org.emonocot.checklist.test;

import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext-test.xml" })
public class DateTimeFormatterTest {
	
	@Autowired
	ConversionService conversionService;
	
	@Test
	public void testDateTimeFormatter() {
		DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();
		try{
			dateTimeFormatter.parseDateTime("2010-06-17T10:45:45.067Z");
		} catch(Exception e) {
			fail("No " + e.getClass().getName() + " expected here");
		}
		
	}

	@Test
	public void testConversionService() {

		try{
			conversionService.convert("2010-06-17T10:45:45.067Z", DateTime.class);
		} catch(Exception e) {
			for(StackTraceElement ste : e.getStackTrace()) {
				System.err.println(ste);
			}
			
			for(StackTraceElement ste : e.getCause().getStackTrace()) {
				System.err.println(ste);
			}
			fail("No " + e.getClass().getName() + " expected here");
		}
		
	}
	
}
