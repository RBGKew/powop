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
package org.powo.persistence.dao.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.SolrDocument;
import org.hibernate.ObjectNotFoundException;
import org.powo.model.Base;
import org.powo.pager.DefaultPageImpl;
import org.powo.pager.Page;
import org.powo.persistence.dao.SearchableDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SearchableDaoImpl<T extends Base> extends DaoImpl<T> implements SearchableDao<T> {

	private final Logger logger = LoggerFactory.getLogger(SearchableDaoImpl.class);

	private SolrClient solrClient = null;

	@Autowired
	public void setSolrClient(SolrClient solrClient) {
		this.solrClient = solrClient;
	}

	/**
	 * Does this DAO search for SearchableObjects?
	 * @return
	 */
	protected boolean isSearchableObject() {
		return true;
	}

	/**
	 *
	 * @param newType
	 *            Set the type of object handled by this class
	 * @param searchTypes
	 *            Set the subclasses of T to be searched for
	 */
	public SearchableDaoImpl(final Class<T> newType) {
		super(newType);
	}


	public final QueryResponse search(SolrQuery solrQuery){ 
		QueryResponse queryResponse = null;
		if(solrQuery != null){
		try {
			queryResponse = solrClient.query(solrQuery);
		} catch (IOException e) {
			logger.error("Error querying solr server: ", e);
			return null;
			
		} catch (SolrServerException e) {
			logger.error("Error querying solr server: ", e);
			return null;	
		}
		
		}
		return queryResponse;
	}
	
	public final Page<T> search(SolrQuery solrQuery, String fetch) throws SolrServerException, IOException {
		QueryResponse queryResponse;
		try {
			queryResponse = solrClient.query(solrQuery);
		} catch (IOException e) {
			logger.error("Error querying solr server: ", e);
			throw new SolrServerException(e);
		}
		List<T> results = new ArrayList<T>();
		for(SolrDocument solrDocument : queryResponse.getResults()) {
			T object = loadObjectForDocument(solrDocument);
			enableProfilePostQuery(object, fetch);
			results.add(object);
		}

		Integer pageSize = solrQuery.getRows();
		Integer pageNumber = solrQuery.getStart() / pageSize ;
		
		Page<T> page = new DefaultPageImpl<T>(results, pageNumber, pageSize);

		return page;
	}

	public SuggesterResponse autocomplete(SolrQuery query) throws SolrServerException, IOException{
		QueryResponse queryResponse = solrClient.query(query);
		return queryResponse.getSuggesterResponse();
	}

	@Override
	public Page<SolrDocument> searchForDocuments(SolrQuery solrQuery) throws SolrServerException, IOException {
		QueryResponse queryResponse = solrClient.query(solrQuery);
		Integer pageSize = solrQuery.getRows();
		Integer pageNumber = solrQuery.getStart() / pageSize ;
		Page<SolrDocument> page = new DefaultPageImpl<SolrDocument>(queryResponse.getResults(), pageNumber, pageSize);

		return page;
	}


	@Override
	public T loadObjectForDocument(SolrDocument solrDocument) {
		try {
			Class clazz = Class.forName((String)solrDocument.getFieldValue("base.class_s"));
			Long id = (Long) solrDocument.getFieldValue("base.id_l");
			T t = (T) getSession().load(clazz, id);
			t.getIdentifier();
			return t;
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("Could not instantiate search result", cnfe);
		} catch (ObjectNotFoundException onfe) {
			return null;
		}
	}
}
