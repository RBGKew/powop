package org.emonocot.portal.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.emonocot.portal.json.MainSearchBuilder;
import org.emonocot.portal.json.SearchResultBuilder;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.collect.BiMap;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.solr.SolrFieldNameMappings;

@Component
public class ResponseBuilder {

	private static final BiMap<String, String> fieldNames = SolrFieldNameMappings.map.inverse();

	private MainSearchBuilder jsonBuilder = new MainSearchBuilder();

	Map<String, Map<String, List<String>>> highlights = new HashMap<String, Map<String, List<String>>>();

	private TaxonService taxonService;

	public MainSearchBuilder buildJsonResponse(QueryResponse queryResponse, TaxonService taxonService) {
		this.taxonService = taxonService;
		setFacets(queryResponse.getFacetQuery());
		jsonBuilder.totalResults((int)queryResponse.getResults().getNumFound());
		highlights = queryResponse.getHighlighting();
		if(queryResponse.getResults() != null && !queryResponse.getResults().isEmpty()) {
			for(SolrDocument document : queryResponse.getResults()) {
				addResult(document);
			}
		}
		@SuppressWarnings("unchecked")
		SimpleOrderedMap<String> params = (SimpleOrderedMap<String>) queryResponse.getResponseHeader().get("params");
		setParams(params);
		return jsonBuilder;

	}

	private void setFacets(Map<String, Integer> facets) {
		for(Entry<String, Integer> facet : facets.entrySet()) {
			jsonBuilder.addFacet(facet.getKey(), facet.getValue());
		}
	}

	private void setParams(SimpleOrderedMap<String> params) {
		Integer start = Integer.parseInt(params.get("start"));
		Integer rows = Integer.parseInt(params.get("rows"));
		jsonBuilder.perPage(rows);
		jsonBuilder.page(start / rows);
		jsonBuilder.totalPages(jsonBuilder.getTotalResults() / rows);
	}

	private void addResult(SolrDocument document) {
		SearchResultBuilder resultBuilder = new SearchResultBuilder();
		Taxon taxon = taxonService.find((Long) document.get("base.id_l"));

		if(taxon != null) {
			resultBuilder.url("/taxon/" + taxon.getIdentifier());
			resultBuilder.name(taxon.getScientificName());
			resultBuilder.accepted(taxon.isAccepted());
			if(taxon.getScientificNameAuthorship() !=null) {
				resultBuilder.author(taxon.getScientificNameAuthorship());
			}

			if(taxon.getTaxonRank().toString() != null) {
				String rank =  WordUtils.capitalizeFully(taxon.getTaxonRank().toString());
				resultBuilder.rank(rank);
			}

			if(highlights.get(document.get("id").toString()) != null) {
				Map<String, List<String>> highlight = highlights.get(document.get("id"));
				if(!highlight.isEmpty()) {
					List<String> snippets = new ArrayList<>();
					for(Entry<String, List<String>> entry : highlight.entrySet()) {
						if(!entry.getValue().isEmpty()) {
							if(fieldNames.containsKey(entry.getKey())) {
								String key = WordUtils.capitalizeFully(fieldNames.get(entry.getKey()));
								snippets.add(" <b>" + key + "</b>: " + entry.getValue().get(0));
							} else {
								snippets.add(" <b>" + entry.getKey() + "</b>: " + entry.getValue().get(0));
							}
						}
					}
					resultBuilder.snippet(Joiner.on("<br/>").join(snippets));
				}
			}

			if(taxon.getImages() != null) {
				for(Image image : taxon.getImages()) {
					resultBuilder.addImage(image.getAccessUri(), image.getCaption());
				}
			}
			jsonBuilder.addResult(resultBuilder);
		}
	}

}
