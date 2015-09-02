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
package org.emonocot.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Searchable;
import org.emonocot.model.SearchableObject;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrIndexingListener implements PostInsertEventListener,
PostUpdateEventListener, PostDeleteEventListener {

	Logger logger = LoggerFactory.getLogger(SolrIndexingListener.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 961123073889114601L;

	private SolrServer solrServer = null;

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public void indexObjects(Collection<? extends Searchable> searchableObjects) {
		List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
		for (Searchable searchable : searchableObjects) {
			documents.add(searchable.toSolrInputDocument());
		}
		try {
			UpdateResponse updateResponse = solrServer.add(documents);
			if (updateResponse.getStatus() != 0) {
				logger.error("Exception adding solr documents " + updateResponse.toString());
				updateResponse = solrServer.rollback();
			} else {
				updateResponse = solrServer.commit(true,true);
			}
		} catch (SolrServerException sse) {
			logger.error(sse.getLocalizedMessage());
			for(StackTraceElement ste : sse.getStackTrace()) {
				logger.error(ste.toString());
			}
		} catch(IOException ioe) {
			logger.error(ioe.getLocalizedMessage());
			for(StackTraceElement ste : ioe.getStackTrace()) {
				logger.error(ste.toString());
			}
		}
	}

	public void indexObject(Searchable searchableObject) {
		List<Searchable> searchableObjects = new ArrayList<Searchable>();
		searchableObjects.add(searchableObject);
		indexObjects(searchableObjects);
	}

	public void deleteObject(Searchable searchableObject) {
		try {
			solrServer.deleteById(searchableObject.getDocumentId());
			solrServer.commit(true,true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		if(Searchable.class.isAssignableFrom(event.getEntity().getClass())) {
			deleteObject((Searchable) event.getEntity());
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		if(Searchable.class.isAssignableFrom(event.getEntity().getClass())) {
			indexObject((Searchable) event.getEntity());
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		if(Searchable.class.isAssignableFrom(event.getEntity().getClass())) {
			indexObject((Searchable) event.getEntity());
		}
	}

}
