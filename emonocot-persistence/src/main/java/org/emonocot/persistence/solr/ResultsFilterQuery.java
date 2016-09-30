package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class ResultsFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		
		switch(value){
		case "all_results":
			break;	
		case "accepted_names":
			query.addFilterQuery("{!tag=taxon.taxonomic_status_s}taxon.taxonomic_status_s:Accepted");
			break;
		case "has_images":
			query.addFilterQuery("{!tag=taxon.images_not_empty_b}taxon.images_not_empty_b:true");
			break;
		case "accepted_names_and_has_images":
			query.addFilterQuery("{!tag=taxon.taxonomic_status_s}taxon.taxonomic_status_s:Accepted");
			query.addFilterQuery("{!tag=taxon.images_not_empty_b}taxon.images_not_empty_b:true");
		}
		
		
	}
}
