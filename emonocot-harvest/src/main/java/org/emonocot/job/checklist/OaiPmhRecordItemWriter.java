package org.emonocot.job.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.orm.hibernate3.HibernateOperations;

/**
 * @author ben
 */
public class OaiPmhRecordItemWriter extends HibernateItemWriter<Taxon> {
	
   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(OaiPmhRecordItemWriter.class);

    @Override
    protected final void doWrite(final HibernateOperations hibernateTemplate,
            final List<? extends Taxon> items) {
        Map<String, Taxon> itemsToDelete = new HashMap<String, Taxon>();
        Map<String, Taxon> itemsToSave = new HashMap<String, Taxon>();
        for (Taxon t : items) {
            if (t.isDeleted()) {
                itemsToDelete.put(t.getIdentifier(), t);
                logger.debug(t.getIdentifier() + " added to delete list");
            } else {
                itemsToSave.put(t.getIdentifier(), t);
                logger.debug(t.getIdentifier() + " added to save list");
            }
        }

        for (Taxon t : itemsToDelete.values()) {
        	if(t.getParent() != null) {
        	    t.getParent().getChildren().remove(t);
                t.setParent(null);
        	}
        	if(t.getAccepted() != null) {
                t.getAccepted().getSynonyms().remove(t);
                t.setAccepted(null);
        	}
        	if(!t.getChildren().isEmpty()) {
        		for(Taxon child : t.getChildren()) {
        			child.setParent(null);
        		}
        		t.getChildren().clear();
        	}
        	if(!t.getSynonyms().isEmpty()) {
        		for(Taxon synonym : t.getSynonyms()) {
        			synonym.setAccepted(null);
        		}
        		t.getSynonyms().clear();
        	}
        }
        List<Taxon> toSave = new ArrayList<Taxon>(itemsToSave.values());
        super.doWrite(hibernateTemplate, toSave);

        hibernateTemplate.deleteAll(itemsToDelete.values());
    }
}
