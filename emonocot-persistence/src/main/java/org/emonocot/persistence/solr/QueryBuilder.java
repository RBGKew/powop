package org.emonocot.persistence.solr;

import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import com.google.common.collect.ImmutableMap;

public class QueryBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/powop_search");


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
		if(queryMappings.containsKey(key)) {
			queryMappings.get(key).addQueryOption(key, value, query);
		} else {
			basicMapper.addQueryOption(key, value, query);
		}
		return this;
	}

	public SolrQuery build (){
		String highlightQuery = "";
		if(query.getFilterQueries() != null){
			for(String filterQuery : query.getFilterQueries()){
				if(!highlightQuery.isEmpty()){
					highlightQuery += " OR " + filterQuery;
				}else{
					highlightQuery += filterQuery;
				}
			}
			query.add("hl.q", highlightQuery);
		}
		return query;
	}
}
