package org.emonocot.harvest.common;

import java.util.List;

import org.emonocot.model.Searchable;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.springframework.batch.item.ItemWriter;

/**
 *
 * @author ben
 *
 */
public class SolrObjectIndexingWriter implements ItemWriter<Searchable> {
    
    private SolrIndexingListener solrIndexingListener;

	/**
	 * @param solrIndexingListener the solrIndexingListener to set
	 */
	public void setSolrIndexingListener(SolrIndexingListener solrIndexingListener) {
		this.solrIndexingListener = solrIndexingListener;
	}

	public void write(List<? extends Searchable> searchables) throws Exception {
        solrIndexingListener.indexObjects(searchables); 
    }
}
