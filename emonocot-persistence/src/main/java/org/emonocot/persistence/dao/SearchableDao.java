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
package org.emonocot.persistence.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.SolrDocument;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.Base;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.Page;

/**
 * @author ben
 * @param <T>
 */
public interface SearchableDao<T extends Base> extends Dao<T> {

	/**
	 * @param solrQuery TODO
	 * @return a Page from the resultset
	 */
	Page<T> search(SolrQuery solrQuery, String fetch) throws SolrServerException, IOException;
	
	QueryResponse search(SolrQuery solrQuery);

	/**
	 *
	 * @param query The query to autocomplete on
	 * @param pageSize The number of matches to return
	 * @param selectedFacets any restrictions on the search
	 * @return a list of match objects
	 */
	SuggesterResponse autocomplete(SolrQuery solrQuery) throws SolrServerException, IOException;

	/**
	 *
	 * @param query
	 * @param pageSize
	 * @param pageNumber
	 * @param selectedFacets
	 * @param sort
	 * @return
	 */
	public Page<SolrDocument> searchForDocuments(SolrQuery solrQuery) throws SolrServerException, IOException;

	/**
	 *
	 * @param solrDocument
	 * @return
	 */
	T loadObjectForDocument(SolrDocument solrDocument);
}
