package org.emonocot.job.delta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.match.Match;
import org.emonocot.api.match.MatchStatus;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.model.Taxon;
import org.springframework.batch.item.ItemWriter;

public class MapBackedTaxonMatcher implements TaxonMatcher, ItemWriter<Taxon> {
	
	Map<String,String> taxonMap = new HashMap<String,String>();

	@Override
	public List<Match<Taxon>> match(String name) {
		List<Match<Taxon>> matches = new ArrayList<Match<Taxon>>();
		if(taxonMap.containsKey(name)) {
		    Taxon taxon = new Taxon();
		    taxon.setScientificName(name);
		    taxon.setIdentifier(taxonMap.get(name));
		    Match<Taxon> match = new Match<Taxon>();
		    match.setInternal(taxon);
		    match.setStatus(MatchStatus.EXACT);
		    matches.add(match);
		} 
		return matches;
	}

	@Override
	public void write(List<? extends Taxon> items) throws Exception {
		for(Taxon item : items) {
			taxonMap.put(item.getScientificName(), item.getIdentifier());
		}
		
	}

}
