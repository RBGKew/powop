package org.emonocot.harvest.common;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.Searchable;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author ben
 *
 */
public class SolrIndexingWriter
    extends HibernateDaoSupport implements ItemWriter<Long> {

    private Class type;
    
    private SolrIndexingListener solrIndexingListener;

    /**
	 * @param type the type to set
	 */
	public void setType(Class type) {
		this.type = type;
	}

	/**
	 * @param solrIndexingListener the solrIndexingListener to set
	 */
	public void setSolrIndexingListener(SolrIndexingListener solrIndexingListener) {
		this.solrIndexingListener = solrIndexingListener;
	}

	public final void write(final List<? extends Long> identifiers) throws Exception {
		List<Searchable> searchables = new ArrayList<Searchable>();
        for (Long l : identifiers) {
        	searchables.add((Searchable)getSession().load(type, l));
            
        }   
        solrIndexingListener.indexObjects(searchables); 
    }
}
