package org.emonocot.persistence.solr;

import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.emonocot.model.solr.SolrFieldNameMappings;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;

public class QueryBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/powop_search");
	private static final BiMap<String, String> fieldNames = SolrFieldNameMappings.map;
	private static final String[] mainQueryFields = {
			"taxon.description_t",
			"taxon.distribution_ss",
			"taxon.family_ss",
			"taxon.genus_ss",
			"taxon.name_published_in_string_s",
			"taxon.scientific_name_authorship_s",
			"taxon.scientific_name_t",
			"taxon.species_ss",
			"taxon.vernacular_names_ss",
	};

	private static final String[] allNamesQueryFields = {
			"taxon.scientific_name_t",
			"taxon.family_ss",
			"taxon.genus_ss",
			"taxon.species_ss",
			"taxon.vernacular_names_ss",
	};

	private static final String[] allCharacteristicFields = {
			"taxon.description_appearance_t",
			"taxon.description_inflorescence_t",
			"taxon.description_fruit_t",
			"taxon.description_leaves_t",
			"taxon.description_flower_t",
			"taxon.description_seed_t",
			"taxon.description_vegitativePropagation_t",
	};

	private static final Map<String, QueryOption> queryMappings = ImmutableMap.<String, QueryOption>builder()
			.put("main.query", new MultiFieldQuery(mainQueryFields))
			.put("all.names", new MultiFieldQuery(allNamesQueryFields))
			.put("taxon.description_t", new MultiFieldQuery(allCharacteristicFields))
			.put("taxon.name_published_in_year_i", new RangeFilterQuery())
			.put("sort", new SortQuery())
			.put("page.number", new pageNumberQuery())
			.put("pageNumber", new pageNumberQuery())
			.put("page.size", new pageSizeQuery())
			.put("pageSize" , new pageSizeQuery())
			.put("base.class_searchable_b", new searchableFilterQuery())
			.put("selectedFacet", new ResultsFilterQuery())
			.build();

	private static final QueryOption basicMapper = new SingleFieldFilterQuery();

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

	public QueryBuilder addHighlightField(String field) {
		query.addHighlightField(field);
		return this;
	}

	public SolrQuery build (){
		return query;
	}
}
