package org.powo.portal.view.helpers;

import java.io.IOException;

import org.powo.model.Reference;
import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;
import org.junit.Before;
import org.junit.Test;
import org.powo.portal.view.Bibliography;
import org.powo.portal.view.Sources;
import org.powo.portal.view.helpers.BibliographyHelper;

import com.github.jknack.handlebars.Handlebars;
import com.google.common.collect.ImmutableSet;

public class BibliographyHelperTest extends AbstractHelperTest {

	Taxon taxon;
	Bibliography bibliography;
	Sources sources;
	Organisation org;

	@Override
	protected Handlebars newHandlebars() {
		Handlebars handlebars = super.newHandlebars();
		handlebars.registerHelpers(new BibliographyHelper());
		return handlebars;
	}

	@Before
	public void setUp() {
		taxon = new Taxon();
		org = new Organisation();
		Reference ref = new Reference();

		org.setIdentifier("1");
		org.setAbbreviation("BI");
		org.setTitle("Blarg Inc.");
		ref.setIdentifier("ref1");

		taxon.setLicense("blarg");
		taxon.setRights("none");
		taxon.setAuthority(org);
		taxon.setReferences(ImmutableSet.<Reference>of(ref));

		bibliography = new Bibliography(taxon);
		sources = new Sources(taxon);
	}

	@Test
	public void testSourceLinkWithTooltip() throws IOException {
		shouldCompileTo("{{sourceLink this}}", org, "<a href=\"#source-BI\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Blarg Inc.\">BI</a>");
	}

	public Taxon getTaxon() {
		return this.taxon;
	}

	public Bibliography getBibliography() {
		return this.bibliography;
	}

	public Sources getSources() {
		return this.sources;
	}
}
