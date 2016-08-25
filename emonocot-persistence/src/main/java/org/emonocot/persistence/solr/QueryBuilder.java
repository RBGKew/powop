package org.emonocot.persistence.solr;

import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import com.google.common.collect.ImmutableMap;

public class QueryBuilder {

	private SolrQuery query = initialQuery();

	private SolrQuery initialQuery (){
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setRows(24);
		query.setStart(0);
		query.set("spellcheck", "true");
		query.set("spellcheck.collate", "true");
		query.set("spellcheck.count", "1");
		query.set("defType","edismax");
		query.set("qf", "searchable.label_sort searchable.solrsummary_t");
		query.addFilterQuery("base.class_searchable_b:true");
		query.addFacetQuery("{!ex=taxon.taxonomic_status_s key=has_images}taxon.images_not_empty_b:true");
		query.addFacetQuery("{!ex=taxon.images_not_empty_b key=accepted_names}taxon.taxonomic_status_s:Accepted");
		query.addFacetQuery("{!ex=taxon.taxonomic_status_s,taxon.images_not_empty_b key=all_results}*:*");
		query.setHighlight(true);
		query.setHighlightFragsize(100);
		query.setHighlightRequireFieldMatch(true);
		query.add("hl.fl", "*");
		return query;
	}

	private static final Map<String, QueryOption> queryMappings = ImmutableMap.<String, QueryOption>builder()
			.put("main.query", new MainFilterQuery())
			.put("all.names", new AllNamesFilterQuery())
			.put("taxon.name_published_in_year_i", new RangeFilterQuery())
			.put("sort", new SortQuery())
			.put("page.number", new pageNumberQuery())
			.put("pageNumber", new pageNumberQuery())
			.put("page.size", new pageSizeQuery())
			.put("pageSize" , new pageSizeQuery())
			.put("taxon.taxonomic_status_s", new tagFilterQuery())
			.put("taxon.images_not_empty_b", new tagFilterQuery())
			.put("base.class_searchable_b", new searchableFilterQuery())
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
		for(String filterQuery : query.getFilterQueries()){
			if(!highlightQuery.isEmpty()){
				highlightQuery += " OR " + filterQuery;
			}else{
				highlightQuery += filterQuery;
			}
		}
		query.add("hl.q", highlightQuery);
		return query;
	}
}
