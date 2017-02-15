package org.emonocot.persistence.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultsFilterQuery implements QueryOption {
	private static Logger logger = LoggerFactory.getLogger(ResultsFilterQuery.class);
	
	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		String[] facets = value.split(" AND ");
		List<String> selectedFacets = new ArrayList<String>();
		for(String facet: facets){
			switch(facet){
			case "all_results":
				break;	
			case "accepted_names":
				selectedFacets.add("taxon.taxonomic_status_s:Accepted");
				break;
			case "has_images":
				selectedFacets.add("taxon.images_not_empty_b:true");
				break;
			case "is_fungi":
				selectedFacets.add("taxon.kingdom_s:Fungi");
			}
			
		}
		if(!selectedFacets.isEmpty()){
			query.add("fq", "{!tag=facets}" + StringUtils.join(selectedFacets, " AND "));
		}
	}
}
 