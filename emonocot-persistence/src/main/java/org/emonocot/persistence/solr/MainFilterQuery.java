package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class MainFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		if(value != null && !value.isEmpty()){
			query.addFilterQuery(String.format("taxon.scientific_name_t:%1$s OR taxon.family_ss:%1$s OR taxon.genus_ss:%1$s OR taxon.species_ss:%1$s OR taxon.vernacular_names_ss:%1$s "
					+ "OR taxon.description_t:%1$s OR taxon.distribution_ss:%1$s OR taxon.name_published_in_string_s:%1$s OR taxon.scientific_name_authorship_s:%1$s" , value));
		}
	}

}

