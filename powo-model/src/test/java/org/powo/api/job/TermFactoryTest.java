package org.powo.api.job;

import static org.junit.Assert.assertEquals;
import static org.powo.api.job.TermFactory.findTerm;

import org.gbif.dwc.terms.AcTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.dwc.terms.XmpRightsTerm;
import org.gbif.dwc.terms.XmpTerm;
import org.junit.Test;
import org.powo.api.job.EmonocotTerm;
import org.powo.api.job.ExifTerm;
import org.powo.api.job.ExtendedAcTerm;
import org.powo.api.job.Iptc4xmpTerm;
import org.powo.api.job.SkosTerm;
import org.powo.api.job.Wgs84Term;

public class TermFactoryTest {

	// Exercise at least one term from each namespace

	@Test
	public void testDwcTerm() {
		assertEquals(findTerm("http://rs.tdwg.org/dwc/terms/Occurrence"), DwcTerm.Occurrence);
	}

	@Test
	public void testDcTerm() {
		assertEquals(findTerm("http://purl.org/dc/elements/1.1/date"), DcTerm.date);
	}

	@Test
	public void testGbifTerm() {
		assertEquals(findTerm("http://rs.gbif.org/terms/1.0/appendixCITES"), GbifTerm.appendixCITES);
	}

	@Test
	public void testAcTerm() {
		assertEquals(findTerm("http://rs.tdwg.org/ac/terms/accessURI"), AcTerm.accessURI);
	}

	@Test
	public void testExtendedAcTerm() {
		assertEquals(findTerm("http://rs.tdwg.org/ac/terms/Image"), ExtendedAcTerm.Image);
	}

	@Test
	public void testIucnTerm() {
		assertEquals(findTerm("http://iucn.org/terms/threatStatus"), IucnTerm.threatStatus);
	}

	@Test
	public void testWgs84Term() {
		assertEquals(findTerm("http://www.w3.org/2003/01/geo/wgs84_pos#latitude"), Wgs84Term.latitude);
	}

	@Test
	public void testEmonocotTerm() {
		assertEquals(findTerm("http://e-monocot.org/subfamily"), EmonocotTerm.subfamily);
	}

	@Test
	public void testSkosTerm() {
		assertEquals(findTerm("http://www.w3.org/2004/02/skos/core#Concept"), SkosTerm.Concept);
	}

	@Test
	public void testExifTerm() {
		assertEquals(findTerm("http://ns.adobe.com/exif/1.0/PixelXDimension"), ExifTerm.PixelXDimension);
	}

	@Test
	public void testIptc4xmpTerm() {
		assertEquals(findTerm("http://iptc.org/std/Iptc4xmpExt/2008-02-29/WorldRegion"), Iptc4xmpTerm.WorldRegion);
	}

	@Test
	public void testXmpTerm() {
		assertEquals(findTerm("http://ns.adobe.com/xap/1.0/Rating"), XmpTerm.Rating);
	}

	@Test
	public void testXmpRightsTerm() {
		assertEquals(findTerm("http://ns.adobe.com/xap/1.0/rights/Owner"), XmpRightsTerm.Owner);
	}
}
