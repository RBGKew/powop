package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

public class SortQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		switch(value){
		case "sort_name_desc":
			query.setSort("taxon.scientific_name_s", ORDER.desc);
			break;
		case "sort_name_asc":
			query.setSort("taxon.scientific_name_s", ORDER.asc);
			break;

		}

	}
}
