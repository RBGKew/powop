package org.emonocot.portal.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.emonocot.portal.json.MainSearchBuilder;
import org.emonocot.portal.json.SearchResultBuilder;
import org.springframework.stereotype.Component;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;

@Component
public class ResponseBuilder {

	private MainSearchBuilder jsonBuilder = new MainSearchBuilder();

	Map<String, Map<String, List<String>>> highlights = new HashMap<String, Map<String, List<String>>>();

	private TaxonService taxonService;

	public MainSearchBuilder buildJsonResponse(QueryResponse queryResponse, TaxonService taxonService){
		this.taxonService = taxonService;
		jsonBuilder.totalResults((int) queryResponse.getResults().getNumFound());
		Map<String, Integer> facets = queryResponse.getFacetQuery();
		for(Entry<String, Integer> facet : facets.entrySet()){
			jsonBuilder.addFacet(facet.getKey(), facet.getValue());
		}
		highlights = queryResponse.getHighlighting();
		if(queryResponse.getResults() != null && !queryResponse.getResults().isEmpty()){
			for(SolrDocument document : queryResponse.getResults()){
				addResult(document);
			}
		}
		return jsonBuilder;

	}


	private void addResult(SolrDocument document){
		SearchResultBuilder resultBuilder = new SearchResultBuilder();
		Taxon taxon = taxonService.find((Long) document.get("base.id_l"));

		if(taxon != null){

			resultBuilder.url("/taxon/" + taxon.getIdentifier());
			resultBuilder.name(taxon.getScientificName());
			if(taxon.getScientificNameAuthorship() !=null){
				resultBuilder.author(taxon.getScientificNameAuthorship());
			}

			if(taxon.getTaxonRank().toString() != null){
				resultBuilder.rank(taxon.getTaxonRank().toString());
			}

			if(highlights.get(document.get("id").toString()) != null){
				resultBuilder.snippet(highlights.get(document.get("id").toString()));
			}


			if(taxon.getImages() != null && !taxon.getImages().isEmpty()){
				for(Image image : taxon.getImages()){
					resultBuilder.addImage(image.getAccessUri(), image.getCaption());
				}
			}
			jsonBuilder.addResult(resultBuilder);
		}
	}

}
