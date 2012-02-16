package org.emonocot.job.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.orm.hibernate3.HibernateOperations;

/**
 * @author ben
 */
public class OaiPmhRecordItemWriter extends HibernateItemWriter<Taxon> {

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

        // Until org.hibernate.annotations.@OnDelete includes a SET NULL option
        for (Taxon t : itemsToDelete.values()) {
            for (Taxon child : t.getChildren()) {
                // See if it's being updated in-chunk
                Taxon savable = itemsToSave.get(child.getIdentifier());
                if (savable == null)
                    savable = child;
                // If it still thinks t is the parent
                if (savable.getParent().getIdentifier()
                        .equals(t.getIdentifier())) {
                    savable.setParent(null);
                    itemsToSave.put(savable.getIdentifier(), savable);
                }
            }
            for (Taxon synonym : t.getSynonyms()) {
                // See if it's being updated in-chunk
                Taxon savable = itemsToSave.get(synonym.getIdentifier());
                if (savable == null)
                    savable = synonym;
                // If it still thinks t is the parent
                if (savable.getAccepted().getIdentifier()
                        .equals(t.getIdentifier())) {
                    savable.setAccepted(null);
                    itemsToSave.put(savable.getIdentifier(), savable);
                }
            }
        }
        List<Taxon> toSave = new ArrayList<Taxon>(itemsToSave.values());
        super.doWrite(hibernateTemplate, toSave);

        hibernateTemplate.deleteAll(itemsToDelete.values());
    }
}
