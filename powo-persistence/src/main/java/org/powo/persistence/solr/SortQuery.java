package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

public class SortQuery extends BaseQueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		switch(value){
		case "name_asc":
			query.addSort("sortable", ORDER.asc);
			query.addSort("id", ORDER.asc);
			break;
		case "name_desc":
			query.addSort("sortable", ORDER.desc);
			query.addSort("id", ORDER.asc);
			break;
		case "relevance":
			query.setSort("score", ORDER.desc);
			query.addSort("sortable", ORDER.asc);
			query.addSort("id", ORDER.asc);
			break;
		}
	}
}
