package org.powo.portal.json;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.IntMath;

import org.powo.model.solr.SolrFieldNameMappings;
import org.powo.portal.json.SearchResponse;
import org.powo.portal.json.SearchResponse.SearchResponseBuilder;
import org.powo.portal.json.SearchResult;
import org.powo.portal.json.SearchResult.SearchResultBuilder;

@Component
public class ResponseBuilder {

	private static Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

	private static final BiMap<String, String> fieldNames = SolrFieldNameMappings.map.inverse();
	private SearchResponseBuilder response = SearchResponse.builder();
	private Map<String, Map<String, List<String>>> highlights = new HashMap<>();

	public SearchResponse buildJsonResponse(QueryResponse queryResponse) {
		highlights = queryResponse.getHighlighting();
		if(queryResponse.getResults() != null && !queryResponse.getResults().isEmpty()) {
			for(SolrDocument document : queryResponse.getResults()) {
				addResult(document);
			}
		}
		setPageInfo(queryResponse);

		return response.build();
	}

	private void setPageInfo(QueryResponse queryResponse) {
		@SuppressWarnings("unchecked")
		SimpleOrderedMap<String> params = (SimpleOrderedMap<String>) queryResponse.getResponseHeader().get("params");
		Integer start = Integer.parseInt(params.get("start"));
		Integer rows = Integer.parseInt(params.get("rows"));
		int totalResults = (int)queryResponse.getResults().getNumFound();

		response.perPage(rows);
		response.page(IntMath.divide(start, rows, RoundingMode.CEILING));
		response.totalPages(IntMath.divide(totalResults, rows, RoundingMode.CEILING));
		response.totalResults(totalResults);
	}

	private void addResult(SolrDocument document) {
		SearchResultBuilder result = SearchResult.builder()
				.url("/taxon/" + getStr(document, "taxon.identifier_s"))
				.name(getStr(document, "taxon.scientific_name_s_lower"))
				.accepted(getBool(document, "taxon.looks_accepted_b"))
				.author(getFirst(document, "taxon.scientific_name_authorship_t"))
				.kingdom(getStr(document, "taxon.kingdom_s_lower"));

		if(!getBool(document, "taxon.is_accepted_b") && document.containsKey("taxon.accepted.identifier_s")) {
			SearchResult synonym = SearchResult.builder()
					.url("/taxon/" + document.get("taxon.accepted.identifier_s"))
					.name(getStr(document, "taxon.accepted.scientific_name_s_lower"))
					.accepted(true)
					.author(getFirst(document, "taxon.accepted.scientific_name_authorship_t"))
					.kingdom(getStr(document, "taxon.accepted.kingdom_s_lower"))
					.build();
			result.synonymOf(synonym);
		}

		if(document.get("taxon.rank_s_lower") != null) {
			String rank =  WordUtils.capitalizeFully((String)document.get("taxon.rank_s_lower"));
			result.rank(rank);
		}

		for(int i = 1; i <= 3; i++) {
			if(document.containsKey("taxon.image_" + i + "_thumbnail_s")) {
				result.addImage(
						getStr(document, "taxon.image_" + i + "_thumbnail_s"),
						getStr(document, "taxon.image_" + i + "_fullsize_s"),
						getStr(document, "taxon.image_" + i + "_caption_s"));
			} else {
				break;
			}
		}

		addSnippets(result, document);

		response.result(result.build());
	}

	private static final ImmutableSet<String> blacklist = ImmutableSet.<String>of(
			"searchable.sources_ss",
			"taxon.family_s_lower",
			"taxon.genus_s_lower",
			"taxon.scientific_name_s_lower",
			"taxon.species_s_lower",
			"taxon.synonyms_ss_lower");

	private void addSnippets(SearchResultBuilder result, SolrDocument document) {
		List<String> snippets = new ArrayList<>();
		if(highlights.get(document.get("id").toString()) != null) {
			Map<String, List<String>> highlight = highlights.get(document.get("id"));
			if(!highlight.isEmpty()) {
				for(Entry<String, List<String>> entry : highlight.entrySet()) {
					if(!entry.getValue().isEmpty()) {
						if(fieldNames.containsKey(entry.getKey()) && !blacklist.contains(entry.getKey())) {
							String key = WordUtils.capitalizeFully(fieldNames.get(entry.getKey()));
							snippets.add(" <b>" + key + "</b>: " + entry.getValue().get(0));
						}
					}
				}
			}
		}
		if(snippets.isEmpty()){
			snippets = defaultSnippet(document);
		}
		if(!snippets.isEmpty()){
			result.snippet(Joiner.on("<br/>").join(snippets));
		}
	}

	private List<String> defaultSnippet(SolrDocument document){
		List<String> snippets = new ArrayList<String>();
		String defaultName = defaultCommonName(document);
		String defaultDescription = defaultDescription(document);
		if(defaultName != null){
			logger.debug("adding default name");
			String key = WordUtils.capitalizeFully(fieldNames.get("taxon.vernacular_names_t"));
			snippets.add(" <b>" + key + "</b>: " + defaultName);
		}
		if(defaultDescription != null){
			snippets.add(" <b>Summary</b>: " + defaultDescription);
		}
		return snippets;
	}

	private String defaultDescription(SolrDocument document){
		if(document.containsKey("taxon.description_snippet_t")){
			return StringUtils.abbreviate(getFirst(document, "taxon.description_snippet_t"), 60);
		}

		return null;
	}

	private String defaultCommonName(SolrDocument document){
		if(document.containsKey("taxon.vernacular_names_t")) {
			return getFirst(document, "taxon.vernacular_names_t");
		}

		return null;
	}

	private String getFirst(SolrDocument document, String key) {
		return (String) document.getFirstValue(key);
	}

	private Boolean getBool(SolrDocument document, String key) {
		return (Boolean) safeGet(document, key);
	}

	private String getStr(SolrDocument document, String key) {
		return (String) safeGet(document, key);
	}

	private Object safeGet(SolrDocument document, String key) {
		if(document != null && document.containsKey(key)) {
			return document.get(key);
		}

		return null;
	}
}

