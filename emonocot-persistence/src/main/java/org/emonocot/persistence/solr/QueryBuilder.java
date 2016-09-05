package org.emonocot.persistence.solr;

import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.emonocot.model.solr.SolrFieldNameMappings;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;

public class QueryBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/powop_search");

	private static final BiMap<String, String> fieldNames = SolrFieldNameMappings.map;

	private static final Map<String, QueryOption> queryMappings = ImmutableMap.<String, QueryOption>builder()
			.put("main.query", new MainFilterQuery())
			.put("all.names", new AllNamesFilterQuery())
			.put("taxon.name_published_in_year_i", new RangeFilterQuery())
			.put("sort", new SortQuery())
			.put("page.number", new pageNumberQuery())
			.put("pageNumber", new pageNumberQuery())
			.put("page.size", new pageSizeQuery())
			.put("pageSize" , new pageSizeQuery())
			.put("base.class_searchable_b", new searchableFilterQuery())
			.put("selectedFacet", new ResultsFilterQuery())
			.build();
	
	
	private static final QueryOption basicMapper = new BasicFieldFilterQuery();

	public QueryBuilder addParam(String key, String value) {
		if(fieldNames.containsKey(key.toLowerCase())){
			key = fieldNames.get(key.toLowerCase());
		}
		if(queryMappings.containsKey(key)) {
			queryMappings.get(key).addQueryOption(key, value, query);
		} else {
			basicMapper.addQueryOption(key, value, query);
		}
		return this;
	}

	public QueryBuilder setHighlightQuery(String key, String value) {
		SolrQuery highlightQuery = new SolrQuery();
		if(fieldNames.containsKey(key.toLowerCase())){
			key = fieldNames.get(key.toLowerCase());
		}
		if(queryMappings.containsKey(key)) {
			queryMappings.get(key).addQueryOption(key, value, highlightQuery);
		} else {
			basicMapper.addQueryOption(key, value, highlightQuery);
		}
		query.add("hl.q", highlightQuery.getFilterQueries());
		return this;
	}
	
	
	public SolrQuery build (){

		return query;
	}
}
