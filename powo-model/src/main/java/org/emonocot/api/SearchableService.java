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
package org.emonocot.api;

import java.io.IOException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.SolrDocument;
import org.emonocot.model.Base;
import org.emonocot.pager.Page;

/**
 * @author ben
 * @param <T>
 */
public interface SearchableService<T extends Base> extends Service<T> {
	/**
	 * @param query
	 *            A lucene query
	 * @param spatialQuery
	 *            A spatial query to filter the results by
	 * @param pageSize
	 *            The maximum number of results to return
	 * @param pageNumber
	 *            The offset (in pageSize chunks, 0-based) from the beginning of
	 *            the recordset
	 * @param facets
	 *            The names of the facets you want to calculate
	 * @param facetPrefixes TODO
	 * @param selectedFacets
	 *            A map of facets which you would like to restrict the search by
	 * @param sort
	 *            a parameter indicating how the results should be sorted
	 * @param fetch
	 *            Set the fetch profile
	 * @return a Page from the resultset
	 */
	Page<T> search(SolrQuery query, String fetch) throws SolrServerException, IOException;
	
	QueryResponse search(SolrQuery solrQuery);

	/**
	 *
	 * @param query The query to autocomplete on
	 * @param pageSize The number of matches to return
	 * @param selectedFacets any restrictions on the search
	 * @return a list of match objects
	 */
	SuggesterResponse autocomplete(SolrQuery query) throws SolrServerException, IOException;

	/**
	 *
	 * @param query
	 * @param pageSize
	 * @param pageNumber
	 * @param selectedFacets
	 * @param sort
	 * @return
	 */
	Page<SolrDocument> searchForDocuments(SolrQuery query) throws SolrServerException, IOException;

	/**
	 *
	 * @param solrDocument
	 * @return
	 */
	T loadObjectForDocument(SolrDocument solrDocument);

	void index(Long id);
}
