package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class AutoCompleteBuilder {

	private SolrQuery query = new SolrQuery().setQuery("*:*");

	private Integer pageSize = 5;



	public AutoCompleteBuilder setQuery(String key, String value) {
		query.setQuery(String.format("%s:%s", key, value));
		return this;
	}

	public AutoCompleteBuilder pageSize(Integer pageSize){
		this.pageSize = pageSize;
		return this;
	}

	public SolrQuery build (){
		query.set("spellcheck", "true");
		query.set("spellcheck.collate", "true");
		query.set("spellcheck.count", "1");
		query.set("defType","edismax");
		query.set("qf", "autocomplete^3 autocompleteng");
		query.set("pf", "autocompletenge");
		query.set("fl","autocomplete,id");
		query.setHighlight(true);
		query.set("hl.fl", "autocomplete");
		query.set("hl.snippets",3);
		query.setHighlightSimplePre("<b>");
		query.setHighlightSimplePost("</b>");
		query.set("group","true");
		query.set("group.field", "autocomplete");
		return query;
	}
}

