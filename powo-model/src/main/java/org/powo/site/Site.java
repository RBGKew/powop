package org.powo.site;

import org.powo.model.solr.QueryOption;

/*
 * Allow running the portal in one of a predefined set of configurations
 *
 * The Site interface encapsulates all the differences between
 * configurations including:
 *
 *  - Background image and site logo
 *  - Blurb text
 *  - Default search parameters and filters
 */
public interface Site {
	public String sitePageClass();
	public void populateTaxonModel();
	public void populateIndexModel();
	public QueryOption defaultQuery();
}
