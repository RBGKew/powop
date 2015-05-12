/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
