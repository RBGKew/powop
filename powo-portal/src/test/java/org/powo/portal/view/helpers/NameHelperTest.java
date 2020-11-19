package org.powo.portal.view.helpers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.powo.model.Taxon;
import org.gbif.ecat.voc.Rank;
import org.junit.Before;
import org.junit.Test;
import org.powo.portal.view.helpers.NameHelper;

import com.github.jknack.handlebars.Handlebars;

public class NameHelperTest extends AbstractHelperTest {

	private Taxon taxon;
	private Taxon family;
	private Taxon genus;
	private Taxon species;
	private Taxon subspecies;

	@Override
	protected Handlebars newHandlebars() {
		Handlebars handlebars = super.newHandlebars();
		handlebars.registerHelpers(new NameHelper());
		return handlebars;
	}

	@Before
	public void setUp() {
		taxon = new Taxon();
		taxon.setIdentifier("urn:lsid:ipni.org:names:1-1");
		taxon.setScientificName("Aralidiaceae");
		taxon.setScientificNameAuthorship("Philipson & B.C.Stone");
		taxon.setTaxonRank(Rank.FAMILY);

		// Set up the following taxnomy
		// Family: Poaceae Barnhart
		//// Genus: Bromus L.
		////// Species: Bromus alopecuros Poir.
		//////// Subspecies: Bromus alopecuros subsp. caroli-henrici (Greuter) P.M.Sm.
		family = new Taxon();
		family.setIdentifier("urn:lsid:ipni.org:names:1-2");
		family.setScientificName("Poaceae");
		family.setScientificNameAuthorship("Barnhart");
		family.setTaxonRank(Rank.FAMILY);

		genus = new Taxon();
		genus.setIdentifier("urn:lsid:ipni.org:names:1-3");
		genus.setScientificName("Bromus");
		genus.setScientificNameAuthorship("L.");
		genus.setTaxonRank(Rank.GENUS);
		genus.setParentNameUsage(family);

		species = new Taxon();
		species.setIdentifier("urn:lsid:ipni.org:names:1-3");
		species.setScientificName("Bromus alopecuros");
		species.setScientificNameAuthorship("Poir.");
		species.setTaxonRank(Rank.SPECIES);
		species.setParentNameUsage(genus);

		subspecies = new Taxon();
		subspecies.setIdentifier("urn:lsid:ipni.org:names:1-3");
		subspecies.setScientificName("Bromus alopecuros subsp. caroli-henrici");
		subspecies.setScientificNameAuthorship("(Greuter) P.M.Sm.");
		subspecies.setTaxonRank(Rank.SUBSPECIES);
		subspecies.setParentNameUsage(genus);
	}

	@Test
	public void testNameAndAuthor() throws IOException {
		shouldCompileTo("{{nameAndAuthor this}}", taxon, "<em lang='la'>Aralidiaceae</em> Philipson & B.C.Stone");
	}

	@Test
	public void testLink() throws IOException {
		shouldCompileTo("{{taxonLink this}}", taxon,
				"<a href=\"/taxon/urn:lsid:ipni.org:names:1-1\"><em lang='la'>Aralidiaceae</em> Philipson & B.C.Stone</a>");
		shouldCompileTo("{{taxonLink this \"Blarg\"}}", taxon, "<a href=\"/taxon/urn:lsid:ipni.org:names:1-1\">Blarg</a>");
	}

	@Test
	public void testClassificationFamilyRendersHeading() throws IOException {
		var result = renderTemplate("{{ classification this }}", family);

		assertEquals("<ul><li>" + "<h1 class=\"c-summary__heading\"><em lang='la'>Poaceae</em> <small>Barnhart</small></h1>"
				+ "</li></ul>", result);
	}

	@Test
	public void testClassificationGenusRendersFamilyLinkAndGenusHeading() throws IOException {
		var result = renderTemplate("{{ classification this }}", genus);

		assertEquals("<ul><li>"
				+ "Family: <a href=\"/taxon/urn:lsid:ipni.org:names:1-2\"><em lang='la'>Poaceae</em> Barnhart</a>" + "<ul><li>"
				+ "<h1 class=\"c-summary__heading\"><em lang='la'>Bromus</em> <small>L.</small></h1></li>" + "</ul></li></ul>",
				result);
	}

	@Test
	public void testClassificationSpeciesRendersParentLinksAndHeading() throws IOException {
		var result = renderTemplate("{{ classification this }}", species);

		assertEquals(
				"<ul><li>Family: <a href=\"/taxon/urn:lsid:ipni.org:names:1-2\"><em lang='la'>Poaceae</em> Barnhart</a>"
						+ "<ul><li>Genus: <a href=\"/taxon/urn:lsid:ipni.org:names:1-3\"><em lang='la'>Bromus</em> L.</a>"
						+ "<ul><li><h1 class=\"c-summary__heading\"><em lang='la'>Bromus alopecuros</em> <small>Poir.</small></h1>"
						+ "</li></ul></li></ul></li></ul>",
				result);
	}

	@Test
	public void testClassificationSubspeciesRendersParentLinksAndHeadingWithRankNotItalic() throws IOException {
		var result = renderTemplate("{{ classification this }}", subspecies);

		assertEquals(
				"<ul><li>Family: <a href=\"/taxon/urn:lsid:ipni.org:names:1-2\"><em lang='la'>Poaceae</em> Barnhart</a>"
						+ "<ul><li>Genus: <a href=\"/taxon/urn:lsid:ipni.org:names:1-3\"><em lang='la'>Bromus</em> L.</a>"
						// Bromus alopecuros subsp. caroli-henrici (Greuter) P.M.Sm.
						+ "<ul><li><h1 class=\"c-summary__heading\"><em lang='la'>Bromus alopecuros</em> subsp. <em lang='la'>caroli-henrici</em> <small>(Greuter) P.M.Sm.</small></h1>"
						+ "</li></ul></li></ul></li></ul>",
				result);
	}
}
