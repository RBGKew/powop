package org.powo.persistence.solr;

import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.model.solr.QueryOption;
import org.powo.model.solr.SolrFieldNameMappings;
import org.powo.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class QueryBuilder {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
	private SolrQuery query = new SolrQuery().setRequestHandler("/powop_search");
	private static final BiMap<String, String> fieldNames = SolrFieldNameMappings.map;

	private static final ImmutableSet<String> allNamesQueryFields = ImmutableSet.<String>of(
			"taxon.scientific_name_s_lower",
			"taxon.synonyms_ss_lower",
			"taxon.family_s_lower",
			"taxon.genus_s_lower",
			"taxon.species_s_lower",
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
			.add("taxon.distribution_ss_lower")
			.add("taxon.name_published_in_s_lower")
			.add("taxon.scientific_name_authorship_s_lower")
			.add("taxon.description_use_t")
			.add("taxon.description_general_t")
			.build();

	private static final Map<String, QueryOption> queryMappings = new ImmutableMap.Builder<String, QueryOption>()
			.put("any", new MultiFieldQuery(mainQueryFields))
			.put("names", new MultiFieldQuery(allNamesQueryFields))
			.put("taxon.description_t", new MultiFieldQuery(allCharacteristicFields))
			.put("taxon.name_published_in_year_i", new RangeFilterQuery())
			.put("sort", new SortQuery())
			.put("page", new PageNumberQuery())
			.put("page.size", new PageSizeQuery())
			.put("base.class_searchable_b", new searchableFilterQuery())
			.put("f", new ResultsFilterQuery())
			.build();
	
	private static final QueryOption basicMapper = new SingleFieldFilterQuery();
	
	public QueryBuilder(){
		query = new SolrQuery().setRequestHandler("/powop_search");
	}
	
	public QueryBuilder(DefaultQueryOption defaultQuery){
		query = new SolrQuery().setRequestHandler("/powop_search");
		defaultQuery.add(query);
	}
	
	public QueryBuilder addParam(String key, String value) {
		if(key.equals("q")) {
			parseQuery(value);
		} else {
			mapParams(key, value);
		}

		return this;
	}

	/* The main query can be a compound query such as q=africa,leaf:compound
	 * It is a comma delimited list of either a single search term or key:value term
	 * terms without a key are treated as if they had key = any */
	private void parseQuery(String q) {
		for(String term : q.split("\\s*,\\s*")) {
			String[] terms = term.split("\\s*:\\s*");
			String key, value;
			if(terms.length == 2) {
				key = terms[0];
				value = terms[1];
			} else {
				key = "any";
				value = terms[0];
			}

			mapParams(key, value);
		}
	}

	private void mapParams(String key, String value) {
		if(fieldNames.containsKey(key.toLowerCase())){
			key = fieldNames.get(key.toLowerCase());
		}

		if(queryMappings.containsKey(key)) {
			queryMappings.get(key).addQueryOption(key, value, query);
		} else {
			basicMapper.addQueryOption(key, value, query);
		}
	}

	public QueryBuilder addHighlightField(String field) {
		query.addHighlightField(field);
		return this;
	}

	public SolrQuery build () {
		return query;
	}
}
