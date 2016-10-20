package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.emonocot.api.job.WCSPTerm;
import org.emonocot.model.Description;
import org.emonocot.model.MeasurementOrFact;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class SummaryTest {

	MessageSource messageSource = new ReloadableResourceBundleMessageSource();
	
	@Test
	public void testFirstPhrase(){
		MeasurementOrFact measurement = new MeasurementOrFact();
		measurement.setMeasurementType(WCSPTerm.Habitat);
		measurement.setMeasurementValue("walls");
		String summary = new SummaryBuilder()
				.messageSource(messageSource)
				.lifeform("scientific monstrosity")
				.habitat(measurement)
				.rank("species")
				.taxonRemarks("western europe")
				.build();
		assertEquals("Found in walls, this scientific monstrosity occurs in western europe.", summary);
	}
	
	@Test
	public void testLifeformAndtaxonremarks(){
		String summary = new SummaryBuilder()
				.messageSource(messageSource)
				.lifeform("scientific monstrosity")
				.taxonRemarks("Western Europe")
				.build();
			assertEquals("This scientific monstrosity occurs in western europe.", summary);
	}
	
	@Test
	public void testHabitat(){
		MeasurementOrFact measurement = new MeasurementOrFact();
		measurement.setMeasurementType(WCSPTerm.Habitat);
		measurement.setMeasurementValue("walls");
		String summary = new SummaryBuilder()
				.messageSource(messageSource)
				.habitat(measurement).build();
		assertEquals("This plant occurs in walls.", summary);
	}
}
