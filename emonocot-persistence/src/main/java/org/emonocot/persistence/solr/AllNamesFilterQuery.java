package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class AllNamesFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		if(value != null && !value.isEmpty()){
			query.addFilterQuery(String.format("taxon.family_ss:%s OR taxon.genus_ss:%s OR taxon.species_ss:%s OR taxon.vernacular_names_ss:%s", value));
		}
	}

}
