package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.powo.model.solr.DefaultQueryOption;

/**
 * This filter ensures that only plants are shown on Plants of the World Online
 */
public class PowoDefaultQuery implements DefaultQueryOption {

	@Override
	public void add(SolrQuery query) {
		query.addFilterQuery("taxon.kingdom_s_lower:Plantae");
	};
}
