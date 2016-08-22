package org.emonocot.model.solr;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class SolrFieldNameMappings {

	public BiMap<String, String> fieldNameMap = new ImmutableBiMap.Builder<String, String>()
			.put("Any", "main.query")
			.put("Family", "taxon.family_ss")
			.put("Genus", "taxon.genus_ss")
			.put("Species", "taxon.species_ss")
			.put("Names", "all.names")
			//placeholder till uses exist
			.put("Uses", "*")
			//.put("")
			.build();
}
