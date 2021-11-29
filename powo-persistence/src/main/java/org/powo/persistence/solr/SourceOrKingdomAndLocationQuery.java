package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.powo.model.solr.DefaultQueryOption;

/**
 * Used by ColPlantASite and ColFungiSite to create a Solr query that includes taxa
 * that are associated to the appropriate organisation (ColPlantA or ColFungi),
 * or are Plantae/Fungi found in Colombia.
 */
public class SourceOrKingdomAndLocationQuery implements DefaultQueryOption {

	private String customQuery;

	public SourceOrKingdomAndLocationQuery(String organisationIdentifier, String kingdom, String location) {
		this.customQuery = "(taxon.kingdom_s_lower:" + kingdom + " AND taxon.distribution_ss_lower:" + location
				+ ") OR searchable.sources_ss:" + organisationIdentifier;
	}

	@Override
	public void add(SolrQuery query) {
		query.addFilterQuery(customQuery);
	};
}
