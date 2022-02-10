package org.powo.portal.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.joda.time.DateTime;
import org.junit.Test;
import org.powo.model.Identification;
import org.powo.test.WithLocale;

public class IdentificationViewTest {
	@Test
	public void testConstructor() throws Exception {
		var identification = new Identification();
		identification.setIdentifiedBy("A");
		identification.setIdentificationReferences("B");
		identification.setTypeStatus("C");

		var identificationView = new IdentificationView(identification);

		assertEquals("A", identificationView.getUrl());
		assertEquals("B", identificationView.getNotes());
		assertEquals("C", identificationView.getTypeStatus());
		assertNull(identificationView.getBarcode());
	}

	@Test
	public void testConstructorWithDateIdentified() throws Exception {
		var identification = new Identification();
		identification.setDateIdentified(DateTime.parse("2010-06-30T00:00"));

		
		try (var locale = new WithLocale(Locale.UK)) {
			var identificationView = new IdentificationView(identification);

			assertEquals("30 Jun 2010", identificationView.getDate());
		}
	}

	@Test
	public void testGetIdentificationsWithIMIBarcode() {
		var identification = new Identification();
		identification.setIdentifiedBy("http://www.herbimi.info/herbimi/specimen.htm?imi=100301");

		var identificationView = new IdentificationView(identification);

		assertEquals("IMI 100301", identificationView.getBarcode());
	}

	@Test
	public void testGetIdentificationsWithHerbtrackBarcode() {
		var identification = new Identification();
		identification.setIdentifiedBy("https://herbtrack.science.kew.org/accession/123645");
		
		var identificationView = new IdentificationView(identification);

		assertEquals("123645", identificationView.getBarcode());
	}

	@Test
	public void testGetIdentificationsWithHerbcatBarcode() {
		var identification = new Identification();
		identification.setIdentifiedBy("http://specimens.kew.org/herbarium/10014.000");

		var identificationView = new IdentificationView(identification);

		assertEquals("10014.000", identificationView.getBarcode());
	}
}
