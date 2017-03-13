package org.emonocot.portal.json;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.emonocot.portal.json.MainSearchBuilder;
import org.emonocot.portal.json.SearchResultBuilder;
import org.emonocot.portal.view.Images;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.IntMath;

import org.emonocot.api.ImageService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.Description;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.solr.SolrFieldNameMappings;

@Component
public class ResponseBuilder {

	private static Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

	private static final BiMap<String, String> fieldNames = SolrFieldNameMappings.map.inverse();

	private MainSearchBuilder jsonBuilder = new MainSearchBuilder();

	Map<String, Map<String, List<String>>> highlights = new HashMap<String, Map<String, List<String>>>();

	private TaxonService taxonService;

	private ImageService imageService;

	public MainSearchBuilder buildJsonResponse(QueryResponse queryResponse, TaxonService taxonService, ImageService imageService) {
		this.taxonService = taxonService;
		this.imageService = imageService;

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
		jsonBuilder.page(IntMath.divide(start, rows, RoundingMode.CEILING));
		jsonBuilder.totalPages(IntMath.divide(jsonBuilder.getTotalResults(), rows, RoundingMode.CEILING));
	}

	private void addResult(SolrDocument document) {
		Taxon taxon = taxonService.find((Long) document.get("base.id_l"));

		if(taxon != null) {
			SearchResultBuilder result = new SearchResultBuilder()
					.url("/taxon/" + taxon.getIdentifier())
					.name(taxon.getScientificName())
					.accepted(taxon.isAccepted())
					.author(taxon.getScientificNameAuthorship())
					.kingdom(taxon.getKingdom());

			if(taxon.getAcceptedNameUsage() != null) {
				SearchResultBuilder synonym = new SearchResultBuilder()
						.url("/taxon/" + taxon.getAcceptedNameUsage().getIdentifier())
						.name(taxon.getAcceptedNameUsage().getScientificName())
						.accepted(true)
						.author(taxon.getAcceptedNameUsage().getScientificNameAuthorship())
						.kingdom(taxon.getKingdom());
				result.synonymOf(synonym);
			}

			if(taxon.getTaxonRank().toString() != null) {
				String rank =  WordUtils.capitalizeFully(taxon.getTaxonRank().toString());
				result.rank(rank);
			}

			Images images = new Images(taxon, imageService);
			for(Image image : images.getAll()) {
				result.addImage(image.getAccessUri(), image.getCaption());
			}

			addSnippets(result, document, taxon);

			jsonBuilder.addResult(result);
		}
	}

	private static final ImmutableSet<String> blacklist = ImmutableSet.<String>of(
			"searchable.sources_ss",
			"taxon.family_ss_lower",
			"taxon.genus_ss_lower",
			"taxon.scientific_name_ss_lower",
			"taxon.species_t",
			"taxon.synonyms_ss_lower");

	private void addSnippets(SearchResultBuilder result, SolrDocument document, Taxon taxon) {
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
			snippets = defaultSnippet(taxon);
		}
		if(!snippets.isEmpty()){
			result.snippet(Joiner.on("<br/>").join(snippets));
		}
	}

	private List<String> defaultSnippet(Taxon taxon){
		List<String> snippets = new ArrayList<String>();
		String defaultName = defaultCommonName(taxon);
		String defaultDescription = defaultDescription(taxon);
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

	private String defaultDescription(Taxon taxon){
		Set<Description> descriptions = taxon.getDescriptions();
		for(Description description : descriptions){
			if(description.getTypes().contains(DescriptionType.snippet)){
				return StringUtils.abbreviate(description.getDescription(), 60);
			}
		}
		return null;
	}

	private String defaultCommonName(Taxon taxon){
		Set<VernacularName> names = taxon.getVernacularNames();
		for(VernacularName name : names){
			if(name.getAuthority().getIdentifier().equals("kew_species_profile_prefered_name")){
				return name.getVernacularName();
			}
		}
		for(VernacularName name : names){
			if(name.getLanguage().equals(Locale.ENGLISH)){
				return name.getVernacularName();
			}
		}
		return null;
	}
}

