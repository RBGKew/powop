package org.emonocot.portal.legacy;

import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.emonocot.persistence.solr.QueryBuilder;

public class OldSearchBuilder {

	public SolrQuery oldSearchBuilder(final String query, final String spatialQuery,
			Integer pageSize,  Integer pageNumber,
			final String[] facets,
			Map<String, String> facetPrefixes, final Map<String, String> selectedFacets,
			final String sort, final String fetch){
		QueryBuilder queryBuilder = new QueryBuilder();
		if(pageNumber == null){
			pageNumber = 0;
		}
		if(pageSize == null){
			pageSize = 0;
		}
		queryBuilder.addParam("pageSize", pageSize.toString());
		queryBuilder.addParam("pageNumber", pageNumber.toString());
		if(sort != null && !sort.isEmpty()){
			queryBuilder.addParam("sort", sort);
		}
		if(selectedFacets != null && !selectedFacets.isEmpty()) {
			for(String facetName : selectedFacets.keySet()) {
				queryBuilder.addParam(facetName, selectedFacets.get(facetName));
			}
		}
		SolrQuery solrQuery = queryBuilder.build();
		if (spatialQuery != null && spatialQuery.trim().length() != 0) {
			solrQuery.addFilterQuery(spatialQuery);
		}
		if (query != null && !query.trim().equals("")) {
			String searchString = null;
			if (query.indexOf(":") != -1) {
				searchString = query;
			} else {
				// replace spaces with '+' so that we search on terms
				searchString = query.trim().replace(" ", "+");
				solrQuery.set("defType","edismax");
				solrQuery.set("qf", "searchable.label_sort searchable.solrsummary_t");
			}
			solrQuery.setQuery(searchString);
		} else {
			solrQuery.set("defType","edismax");
			solrQuery.set("qf", "searchable.label_sort searchable.solrsummary_t");
			solrQuery.setQuery("*:*");
		}
		return solrQuery;
	}

	public SolrQuery oldAutocomplete(final String query, final Integer pageSize, Map<String, String> selectedFacets){
		QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.addParam("pageSize", pageSize.toString());
		if(selectedFacets != null && !selectedFacets.isEmpty()) {
			for(String facetName : selectedFacets.keySet()) {
				queryBuilder.addParam(facetName, selectedFacets.get(facetName));
			}
		}
		SolrQuery solrQuery = queryBuilder.build();
		solrQuery.setQuery(query);
		return solrQuery;

	}
}