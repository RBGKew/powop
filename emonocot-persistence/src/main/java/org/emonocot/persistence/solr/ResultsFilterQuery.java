package org.emonocot.persistence.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gbif.ecat.voc.Rank;

public class ResultsFilterQuery extends QueryOption {
	private static Logger logger = LoggerFactory.getLogger(ResultsFilterQuery.class);

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		String[] facets = value.split(",");
		List<String> selectedFacets = new ArrayList<String>();
		List<Rank> selectedRanks = new ArrayList<Rank>();
		for(String facet: facets){
			switch(facet){
			case "all_results":
				break;
			case "accepted_names":
				selectedFacets.add("taxon.is_accepted_b:true");
				break;
			case "has_images":
				selectedFacets.add("taxon.images_not_empty_b:true");
				break;
			case "is_fungi":
				selectedFacets.add("taxon.kingdom_s_lower:Fungi");
				break;
			case "family_f":
				selectedRanks.add(Rank.FAMILY);
				break;
			case "genus_f":
				selectedRanks.add(Rank.GENUS);
				break;
			case "species_f":
				selectedRanks.add(Rank.SPECIES);
				break;
			case "infraspecific_f":
				selectedRanks.add(Rank.SUBSPECIES);
				selectedRanks.add(Rank.InfraspecificName);
				selectedRanks.add(Rank.InfrasubspecificName);
				selectedRanks.add(Rank.VARIETY);
				selectedRanks.add(Rank.Subvariety);
				selectedRanks.add(Rank.Form);
				selectedRanks.add(Rank.Subform);
				break;
			}
		}

		if(!selectedRanks.isEmpty()){
			String string = "";
			for(Rank rank : selectedRanks){
				string += ObjectUtils.toString(rank, null) + " ";
			}
			selectedFacets.add(String.format("taxon.rank_s_lower: (%s)", string));
		}

		if(!selectedFacets.isEmpty()){
			query.add("fq", "{!tag=facets}" + StringUtils.join(selectedFacets, " AND "));
		}
	}
}
