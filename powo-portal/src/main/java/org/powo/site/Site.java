package org.powo.site;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.portal.view.components.Link;

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
	public Map<String,String> getFormattedTaxonCounts();
	public DefaultQueryOption defaultQuery();
	public List<String> getSuggesters();
	public String suggesterFilter();
	public Locale defaultLocale();
	String siteId();
	String kewLogoPath();
	String indexPageTitle();
	String taxonPageTitle(Taxon taxon);
	String favicon();
	Optional<Link> crossSiteLink();
}
