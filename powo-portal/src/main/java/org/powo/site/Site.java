package org.powo.site;

import java.util.List;
import java.util.Locale;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.springframework.ui.Model;

/*
 * Allow running the portal in one of a predefined set of configurations
 *
 * The Site interface encapsulates all the differences between
 * configurations including:
 *
 *  - Background image and site logo
 *  - Blurb text
 *  - Default search parameters and filters
 *  - Available suggesters
 */
public interface Site {
	public void populateTaxonModel(Taxon taxon, Model model);
	public void populateIndexModel(Model model);
	public void populateStaticModel(Model model);
	public DefaultQueryOption defaultQuery();
	public List<String> getSuggesters();
	public String suggesterFilter();
	public Locale defaultLocale();
	String indexPageTitle();
	String taxonPageTitle(Taxon taxon);
	String favicon();
}
