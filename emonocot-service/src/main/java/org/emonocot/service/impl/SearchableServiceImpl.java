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
package org.emonocot.service.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.SolrDocument;
import org.emonocot.api.SearchableService;
import org.emonocot.model.Base;
import org.emonocot.model.Searchable;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.SearchableDao;
import org.emonocot.persistence.hibernate.SolrIndexingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 * @param <T> the type of object provided by this service
 * @param <DAO> the DAO used by this service
 */
public abstract class SearchableServiceImpl<T extends Base, DAO extends SearchableDao<T>>
extends ServiceImpl<T, DAO> implements SearchableService<T> {

	private SolrIndexingInterceptor solrIndexingInterceptor;

	@Autowired
	public void setSolrIndexingInterceptor(SolrIndexingInterceptor solrIndexingInterceptor) {
		this.solrIndexingInterceptor = solrIndexingInterceptor;
	}

	/**
	 * @param query
	 *            Set the lucene query
	 * @param spatialQuery
	 *            Set the spatial query
	 * @param pageSize
	 *            Set the maximum number of objects to return
	 * @param pageNumber
	 *            Set the offset (in 'pageNumber' chunks) from the start of the
	 *            resultset (0-based!)
	 * @param facets
	 *            Set the facets to calculate
	 * @param selectedFacets
	 *            Set the facets to select, restricing the results of the query
	 * @param sort Set the sort order
	 * @param fetch Set the fetch profile
	 * @return a page of results
	 */
	@Transactional(readOnly = true)
	public final Page<T> search(final SolrQuery query, final String fetch)  throws SolrServerException, IOException {
		return dao.search(query, fetch );
	}
	
	@Transactional(readOnly = true)
	public final QueryResponse search(final SolrQuery query) {
		return dao.search(query);
	}

	@Transactional(readOnly = true)
	public SuggesterResponse autocomplete(final SolrQuery query)  throws SolrServerException, IOException {
		return dao.autocomplete(query);
	}

	@Transactional(readOnly = true)
	public Page<SolrDocument> searchForDocuments(SolrQuery query)  throws SolrServerException, IOException {
		return dao.searchForDocuments(query);
	}


	@Transactional(readOnly = true)
	public T loadObjectForDocument(SolrDocument solrDocument) {
		return dao.loadObjectForDocument(solrDocument);
	}

	@Transactional(readOnly = true)
	public CellSet analyse(String rows, String cols, Integer firstCol, Integer maxCols, Integer firstRow, Integer maxRows,	Map<String, String> selectedFacets, String[] array, Cube cube)  throws SolrServerException, IOException {
		return dao.analyse(rows, cols, firstCol, maxCols, firstRow, maxRows,selectedFacets, array, cube);
	}

	@Override
	@Transactional(readOnly = true)
	public void index(Long id) {
		T t = load(id);
		solrIndexingInterceptor.indexObject((Searchable)t);
	}
}
