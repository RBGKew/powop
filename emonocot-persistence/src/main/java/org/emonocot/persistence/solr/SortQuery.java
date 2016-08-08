package org.emonocot.persistence.solr;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

public class SortQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		if(value.endsWith("_desc")){
			value = StringUtils.removeEnd(value, "_desc");
			query.setSort(value, ORDER.desc);
		}else{
			value = StringUtils.removeEnd(value, "_asc");
			query.setSort(value, ORDER.asc);
		}

	}
}
