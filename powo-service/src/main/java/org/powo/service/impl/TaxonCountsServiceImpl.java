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
package org.powo.service.impl;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.powo.api.SearchableObjectService;
import org.powo.api.TaxonCountsService;
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
public class TaxonCountsServiceImpl implements TaxonCountsService {

	private static final Map<String, String> facetQueries = new ImmutableMap.Builder<String, String>()
			.put("species", "taxon.is_accepted_b:true AND taxon.rank_s_lower:species")
			.put("genus", "taxon.is_accepted_b:true AND taxon.rank_s_lower:genus")
			.put("family", "taxon.is_accepted_b:true AND taxon.rank_s_lower:family").build();

	@Autowired
	private SearchableObjectService searchableObjectService;

	@Override
	public TaxonCounts get(DefaultQueryOption defaultQuery) {
		SolrQuery query = new QueryBuilder(defaultQuery, Collections.emptyMap()).build();
		query.addFacetQuery(facetQueries.get("species"));
		query.addFacetQuery(facetQueries.get("genus"));
		query.addFacetQuery(facetQueries.get("family"));
		QueryResponse queryResponse = searchableObjectService.search(query);
		Map<String, Integer> facetCounts = queryResponse.getFacetQuery();

		Long totalCount = queryResponse.getResults().getNumFound();
		Integer speciesCount = facetCounts.get(facetQueries.get("species"));
		Integer genusCount = facetCounts.get(facetQueries.get("genus"));
		Integer familyCount = facetCounts.get(facetQueries.get("family"));

		return new TaxonCounts(totalCount, speciesCount, genusCount, familyCount);
	}

}
