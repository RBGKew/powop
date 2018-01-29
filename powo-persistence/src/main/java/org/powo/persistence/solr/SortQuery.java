package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

public class SortQuery extends QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		switch(value){
		case "name_asc":
			query.setSort("taxon.rank_s_lower", ORDER.asc);
			query.addSort("sortable", ORDER.asc);
			break;
		case "name_desc":
			query.setSort("taxon.rank_s_lower", ORDER.asc);
			query.addSort("sortable", ORDER.desc);
			break;
		case "relevance":
			query.setSort("score", ORDER.desc);
			query.addSort("taxon.rank_s_lower", ORDER.asc);
			query.addSort("sortable", ORDER.asc);
			break;
		}
	}
}
