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
package org.powo.portal.service;

import java.util.Collections;

import org.powo.api.SearchableObjectService;
import org.powo.model.TaxonCounts;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author joaquim
 *
 */
@Service
public class TaxonCountsService {
  private static final String speciesQuery = "taxon.is_accepted_b:true AND taxon.rank_s_lower:species";
  private static final String genusQuery = "taxon.is_accepted_b:true AND taxon.rank_s_lower:genus";
  private static final String familyQuery = "taxon.is_accepted_b:true AND taxon.rank_s_lower:family";

	@Autowired
	private SearchableObjectService searchableObjectService;

	public TaxonCounts get(DefaultQueryOption defaultQuery) {
		var query = new QueryBuilder(defaultQuery, Collections.emptyMap()).build();
		query.addFacetQuery(speciesQuery);
		query.addFacetQuery(genusQuery);
		query.addFacetQuery(familyQuery);
		var queryResponse = searchableObjectService.search(query);
		var facetCounts = queryResponse.getFacetQuery();

		var totalCount = queryResponse.getResults().getNumFound();
		var speciesCount = facetCounts.get(speciesQuery);
		var genusCount = facetCounts.get(genusQuery);
		var familyCount = facetCounts.get(familyQuery);

		return new TaxonCounts(totalCount, speciesCount, genusCount, familyCount);
	}

}
