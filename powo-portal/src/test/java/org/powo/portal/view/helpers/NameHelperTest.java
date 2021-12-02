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
}
