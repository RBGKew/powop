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
package org.emonocot.portal.remoting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.SolrDocument;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.Base;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public abstract class DaoImpl<T extends Base> implements Dao<T> {

	protected Map<String, String> createdObjects = new HashMap<String,String>();

	/**
	 * Logger.
	 */
	private static Logger logger
	= LoggerFactory.getLogger(DaoImpl.class);

	/**
	 *
	 * @param identifier
	 * @return
	 */
	public String getPageLocation(String identifier) {
		return createdObjects.get(identifier);
	}

	/**
	 *
	 * @param identifier
	 * @return
	 */
	public String getIdentifier(String location) {
		for(String identifier : createdObjects.keySet()) {
			if(createdObjects.get(identifier).equals(location)) {
				return identifier;
			}
		}
		return null;
	}

	/**
	 *
	 * @param identifier
	 * @return
	 */
	public boolean containsPageLocation(String location) {
		return createdObjects.containsValue(location);
	}


	/**
	 *
	 */
	protected static HttpHeaders httpHeaders = new HttpHeaders();

	static {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(acceptableMediaTypes);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	}

	/**
	 *
	 */
	 protected RestTemplate restTemplate;

	 /**
	  *
	  */
	 private Class<T> type;

	 /**
	  *
	  */
	 protected String baseUri;

	 /**
	  *
	  */
	 protected String resourceDir;

	 /**
	  * @param restTemplate Set the rest template
	  */
	 @Autowired
	 public final void setRestTemplate(final RestTemplate restTemplate) {
		 this.restTemplate = restTemplate;
	 }

	 /**
	  *
	  * @param newType Set the type object handled by this dao
	  * @param newResourceDir Set the resource directory
	  */
	 public DaoImpl(final Class<T> newType, final String newResourceDir) {
		 this.type = newType;
		 this.resourceDir = newResourceDir;
	 }

	 /**
	  *
	  * @param newBaseUri Set the base uri
	  */
	 public final void setBaseUri(final String newBaseUri) {
		 this.baseUri = newBaseUri;
	 }

	 /**
	  * @param identifier set the identifier
	  * @return an object
	  */
	 public final T load(final String identifier) {
		 HttpEntity<T> requestEntity = new HttpEntity<T>(httpHeaders);
		 HttpEntity<T> responseEntity = restTemplate.exchange(baseUri
				 + resourceDir + "/" + identifier, HttpMethod.GET,
				 requestEntity, type);
		 return responseEntity.getBody();
	 }

	 /**
	  * @param identifier set the identifier
	  * @return an object
	  */
	 public final T load(final Long id) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 /**
	  * @param identifier set the identifier
	  * @return an object
	  */
	 public final T find(final Long id) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 /**
	  * @param identifier set the identifier
	  * @return an object
	  */
	 public final T load(final Long id, final String fetch) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 /**
	  * @param identifier set the identifier
	  * @return an object
	  */
	 public final T find(final Long id, final String fetch) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 public final void deleteById(final Long id) {

	 }

	 /**
	  * @param identifier the identifier of the object to delete
	  */
	 public final void delete(final String identifier) {
		 HttpEntity<T> requestEntity = new HttpEntity<T>(httpHeaders);
		 logger.debug("DELETE: " + createdObjects.get(identifier));
		 if(createdObjects.containsKey(identifier)) {
			 restTemplate.exchange(createdObjects.get(identifier),
					 HttpMethod.DELETE, requestEntity, type);
			 createdObjects.remove(identifier);
		 } else {
			 T t = find(identifier);
			 restTemplate.exchange(baseUri + "/" + resourceDir + "/" + t.getId(),
					 HttpMethod.DELETE, requestEntity, type);
		 }

	 }

	 /**
	  * @param identifier Set the identifier
	  * @return an object
	  */
	 public final T find(final String identifier) {
		 HttpEntity<T> requestEntity = new HttpEntity<T>(httpHeaders);
		 HttpEntity<T> responseEntity = restTemplate.exchange(baseUri + "/"
				 + resourceDir + "/" + identifier, HttpMethod.GET,
				 requestEntity, type);
		 return responseEntity.getBody();
	 }

	 /**
	  * @param identifier set the identifier
	  * @param fetch set the fetch profile
	  * @return an object
	  */
	 public final T load(final String identifier, final String fetch) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 /**
	  * @param identifier set the identifier
	  * @param fetch set the fetch profile
	  * @return an object
	  */
	 public final T find(final String identifier, final String fetch) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 /**
	  * @param t the object to save
	  * @return the saved object
	  */
	 public final T save(final T t) {
		 logger.debug("POST: " + baseUri + resourceDir);
		 HttpEntity<T> requestEntity = new HttpEntity<T>(t, httpHeaders);
		 HttpEntity<T> responseEntity = restTemplate.exchange(baseUri + "/"
				 + resourceDir, HttpMethod.POST,
				 requestEntity, type);
		 createdObjects.put(t.getIdentifier(), responseEntity.getHeaders().getLocation().toString());
		 return responseEntity.getBody();
	 }
	 /**
	  * @param t the object to update
	  */
	 public final void update(final T t) {
		 logger.debug("POST: " + baseUri + resourceDir + "/" + t.getIdentifier());
		 HttpEntity<T> requestEntity = new HttpEntity<T>(t, httpHeaders);
		 restTemplate.exchange(baseUri
				 + resourceDir + "/" + t.getIdentifier(), HttpMethod.POST,
				 requestEntity, type);
	 }

	 /**
	  * @param t the object to save or update
	  */
	 public void saveOrUpdate(final T t) {
		 // TODO Auto-generated method stub
	 }

	 /**
	  *
	  * @param t
	  *            The object to merge.
	  * @return the merged object
	  */
	 public final T merge(final T t) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 /**
	  * @param solrQuery TODO
	 * @return a page of objects
	  */
	 public final Page<T> search(SolrQuery solrQuery, String fetch) {
		 // TODO Auto-generated method stub
		 // TODO move persistence to persistence package?
		 return null;
	 }

	 /**
	  * @return the total number of objects
	  */
	 public final Long count() {
		 return page(null, null).getSize().longValue();
	 }

	 protected Page<T> page(Integer page, Integer size) {
		 return null;
	 }

	 /**
	  * @param page Set the offset (in size chunks, 0-based), optional
	  * @param size Set the page size
	  * @param fetch The profile describing which objects to hydrate/retrieve
	  * @return A list of results
	  */
	 public List<T> list(final Integer page, final Integer size, final String fetch) {
		 return null;
	 }

	 public SuggesterResponse autocomplete(SolrQuery query) {
		 return null;
	 }

	 public Page<SolrDocument> searchForDocuments(SolrQuery query) {
		 throw new UnsupportedOperationException("Method should be overridden if supported. Unable to search for documents with query" + query);
	 }

	 public T loadObjectForDocument(SolrDocument solrDocument) {
		 throw new UnsupportedOperationException("Method should be overridden if supported. Unable to load object for document " + solrDocument);
	 }

}
