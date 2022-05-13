package org.powo.site;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.portal.view.FeaturedTaxaSection;

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
	Map<String,String> getFormattedTaxonCounts();
	DefaultQueryOption defaultQuery();
	List<String> getSuggesters();
	String suggesterFilter();
	Locale defaultLocale();
	String siteId();
	String oneTrustID();
	String kewLogoPath();
	String favicon();
	/**
	 * The Canonical URL of the homepage of the site.
	 * Not including any trailing slashes.
	 * @return the URL
	 */
	String canonicalUrl();
	List<FeaturedTaxaSection> featuredTaxaSections();
	Organisation primarySource();
	/**
	 * Return true if the provided taxon should be
	 * displayed on this site, false otherwise.
	 */
	boolean hasTaxon(Taxon taxon);
}