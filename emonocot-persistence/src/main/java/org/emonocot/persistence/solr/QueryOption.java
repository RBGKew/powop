package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public interface QueryOption {

	public void addQueryOption(String key, String value, SolrQuery query);

}
