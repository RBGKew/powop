package org.emonocot.persistence.solr;

import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.emonocot.model.solr.SolrFieldNameMappings;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class QueryBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/powop_search");
	private static final BiMap<String, String> fieldNames = SolrFieldNameMappings.map;

	private static final ImmutableSet<String> allNamesQueryFields = ImmutableSet.<String>of(
			"taxon.scientific_name_t",
			"taxon.synonyms_t",
			"taxon.family_t",
			"taxon.genus_t",
			"taxon.species_t",
			"taxon.vernacular_names_t");

	private static final ImmutableSet<String> allCharacteristicFields = ImmutableSet.<String>of(
			"taxon.description_appearance_t",
			"taxon.description_inflorescence_t",
			"taxon.description_fruit_t",
			"taxon.description_leaves_t",
			"taxon.description_flower_t",
			"taxon.description_seed_t",
			"taxon.description_vegitativePropagation_t");

	private static final ImmutableSet<String> mainQueryFields = new ImmutableSet.Builder<String>()
			.addAll(allNamesQueryFields)
			.addAll(allCharacteristicFields)
			.add("taxon.distribution_t")
			.add("taxon.name_published_in_string_s")
			.add("taxon.scientific_name_authorship_t")
			.add("taxon.description_use_t")
			.build();

	private static final Map<String, QueryOption> queryMappings = new ImmutableMap.Builder<String, QueryOption>()
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
