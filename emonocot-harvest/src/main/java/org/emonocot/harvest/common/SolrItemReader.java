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

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.SearchableService;
import org.emonocot.model.SearchableObject;
import org.springframework.batch.item.database.AbstractPagingItemReader;

/**
 * @author jk
 *
 */
public class SolrItemReader<T extends SearchableObject> extends AbstractPagingItemReader<T> {

	private SearchableService<T> service = null;

	private String queryString = null;

	private String spatialString = null;

	private Map<String,String> selectedFacets = new HashMap<String,String>();

	private String sort = null;

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(SearchableService<T> service) {
		this.service = service;
		logger.info("Service set " + service.toString());
	}

	public void setSelectedFacets(String[] selectedFacets) {
		if(selectedFacets != null) {
			for(String selectedFacet : selectedFacets) {
				String[] f = selectedFacet.split("\\=");
				this.selectedFacets.put(f[0],f[1]);
			}
		}
	}


	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setSpatialString(String spatialString) {
		this.spatialString = spatialString;
	}


	@Override
	protected void doReadPage() {
		try {
			results = service.search(queryString, spatialString, getPageSize(), getPage(), null, null, selectedFacets, sort, "object-page").getRecords();
		} catch (SolrServerException sse) {
			throw new RuntimeException("SolrServerException", sse);
		}
		logger.debug("Search for " + queryString + " (page number " + getPage() + " got a page of " + results.size()
				+ " (results");
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.AbstractPagingItemReader#doJumpToPage(int)
	 */
	@Override
	protected void doJumpToPage(int itemIndex) {
	}

}
