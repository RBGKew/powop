package org.emonocot.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Searchable;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.geography.GeographicalRegionFactory;
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
	
	private GeographicalRegionFactory geographicalRegionFactory;
	
	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}	
	
	public void setGeographicalRegionFactory(
			GeographicalRegionFactory geographicalRegionFactory) {
		this.geographicalRegionFactory = geographicalRegionFactory;
	}



	public void indexObjects(Collection<? extends Searchable> searchableObjects) {
		List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
		for (Searchable searchable : searchableObjects) {
			documents.add(searchable.toSolrInputDocument(geographicalRegionFactory));
		}
		try {
			UpdateResponse updateResponse = solrServer.add(documents);
			logger.trace(updateResponse.toString());
			updateResponse = solrServer.commit();
			logger.trace(updateResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void onPostDelete(PostDeleteEvent event) {		
		if(SearchableObject.class.isAssignableFrom(event.getEntity().getClass())) {
		    deleteObject((SearchableObject) event.getEntity());
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
