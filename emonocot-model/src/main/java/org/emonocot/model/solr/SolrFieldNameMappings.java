package org.emonocot.model.solr;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class SolrFieldNameMappings {

	public static final BiMap<String, String> map = new ImmutableBiMap.Builder<String, String>()
			.put("all names", "all.names")
			.put("any characteristic", "taxon.description_t")
			.put("any", "main.query")
			.put("appearance", "taxon.description_appearance_t")
			.put("author", "taxon.scientific_name_authorship_t")
			.put("cloning", "taxon.description_vegitativePropagation_t")
			.put("common name", "taxon.vernacular_names_t")
			.put("family", "taxon.family_t")
			.put("flowers", "taxon.description_flower_t")
			.put("fruit", "taxon.description_fruit_t")
			.put("genus", "taxon.genus_t")
			.put("inflorescence", "taxon.description_inflorescence_t")
			.put("leaves", "taxon.description_leaves_t")
			.put("location", "taxon.distribution_t")
			.put("name", "taxon.scientific_name_t")
			.put("seed", "taxon.description_seed_t")
			.put("species", "taxon.species_t")
			.put("synonym of", "taxon.synonyms_t")
			.put("use", "taxon.description_use_t")
			.build();
}