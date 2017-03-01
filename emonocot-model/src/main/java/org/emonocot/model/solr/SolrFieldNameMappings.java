package org.emonocot.model.solr;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class SolrFieldNameMappings {

	public static final BiMap<String, String> map = new ImmutableBiMap.Builder<String, String>()
			.put("all names", "all.names")
			.put("appearance", "taxon.description_appearance_t")
			.put("author", "taxon.scientific_name_authorship_t")
			.put("characteristic", "taxon.description_t")
			.put("cloning", "taxon.description_vegitativePropagation_t")
			.put("common name", "taxon.vernacular_names_t")
			.put("family", "taxon.family_ss_lower")
			.put("flower", "taxon.description_flower_t")
			.put("fruit", "taxon.description_fruit_t")
			.put("genus", "taxon.genus_ss_lower")
			.put("inflorescence", "taxon.description_inflorescence_t")
			.put("kingdom", "taxon.kingdom_s")
			.put("leaf", "taxon.description_leaf_t")
			.put("location", "taxon.distribution_ss_lower")
			.put("name", "taxon.scientific_name_ss_lower")
			.put("seed", "taxon.description_seed_t")
			.put("source", "searchable.sources_ss")
			.put("species", "taxon.species_t")
			.put("summary", "taxon.description_snippet_t")
			.put("synonym of", "taxon.synonyms_ss_lower")
			.put("use", "taxon.description_use_t")
			.build();
}