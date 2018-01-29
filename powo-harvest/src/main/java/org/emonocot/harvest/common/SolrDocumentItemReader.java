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
package org.emonocot.harvest.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.persistence.solr.QueryBuilder;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;

public class SolrDocumentItemReader extends AbstractPagingItemReader<SolrDocument> {

	private SearchableObjectService searchableObjectService;

	private String queryString = null;

	private String sort = null;
	
	private QueryBuilder queryBuilder = new QueryBuilder();

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setSelectedFacets(String[] selectedFacets) {
		if(selectedFacets != null) {
			for(String selectedFacet : selectedFacets) {
				String[] f = selectedFacet.split("\\=");
				queryBuilder.addParam(f[0],f[1]);
			}
		}
	}


	@Autowired
	public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
		this.searchableObjectService = searchableObjectService;
	}

	@Override
	protected void doReadPage() {
		queryBuilder = new QueryBuilder();
		if(queryString != null && !queryString.isEmpty()){
			queryBuilder.addParam("*", queryString);
		}
		if(sort != null && !sort.isEmpty()){
			queryBuilder.addParam("sort", sort);
		}
		
		queryBuilder.addParam("page.size", "" + getPageSize());
		queryBuilder.addParam("page", "" + getPage());
		SolrQuery query = queryBuilder.build();
		logger.debug(query.toString());
		try {
			results = searchableObjectService.searchForDocuments(query).getResults();
			logger.debug(results.size());
		} catch (SolrServerException | IOException sse) {
			throw new RuntimeException("SolrServerException", sse);
		}

		logger.debug("Search for " + query + " (page number " + getPage() + " got a page of " + results.size()
				+ " (results");
	}

	@Override
	protected void doJumpToPage(int itemIndex) {
	}

}
