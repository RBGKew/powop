package org.powo.persistence.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import com.google.common.base.Joiner;

import org.gbif.ecat.voc.Rank;

public class ResultsFilterQuery extends BaseQueryOption {
	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		String[] facets = value.split(",");
		List<String> selectedFacets = new ArrayList<String>();
		List<Rank> selectedRanks = new ArrayList<Rank>();
		for(String facet: facets){
			switch(facet){
			case "accepted_names":
				selectedFacets.add("taxon.is_accepted_b:true AND taxon.is_unplaced_b:false");
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
			selectedFacets.add(String.format("taxon.rank_s_lower: (%s)", Joiner.on(" OR ").join(selectedRanks)));
		}

		if(!selectedFacets.isEmpty()){
			query.addFilterQuery(Joiner.on(" AND ").join(selectedFacets));
		}
	}
}
