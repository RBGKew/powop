package org.emonocot.job.checklist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Taxon> itemsToDelete = new ArrayList<Taxon>();
        List<Taxon> itemsToSave = new ArrayList<Taxon>();
        for (Taxon t : items) {
            if (t.isDeleted()) {
                itemsToDelete.add(t);
                logger.debug(t.getIdentifier() + " added to delete list");
            } else {
                itemsToSave.add(t);
                logger.debug(t.getIdentifier() + " added to save list");
            }
        }

        // Until org.hibernate.annotations.@OnDelete includes a SET NULL option
        Set<Taxon> childTaxa = new HashSet<Taxon>();
        Set<Taxon> synonyms = new HashSet<Taxon>();
        for (Taxon t : itemsToDelete) {
            childTaxa.addAll(t.getChildren());
            synonyms.addAll(t.getSynonyms());
        }

        // don't update one's we're deleting
        for (Taxon t : itemsToDelete) {
            childTaxa.remove(t);
            synonyms.remove(t);
        }

        for (Taxon c : childTaxa) {
            if (itemsToDelete.contains(c.getParent())) {
                c.setParent(null);
            }
            // add it to itemsToSave if it's not there already
            if (itemsToSave.indexOf(c) < 0) {
                itemsToSave.add(c);
            }
        }

        for (Taxon s : synonyms) {
            if (itemsToDelete.contains(s.getAccepted())) { // if sysnonym's accepted name is about to be deleted 
                s.setAccepted(null);
            }
            // add it to itemsToSave if it's not there already
            if (itemsToSave.indexOf(s) < 0) {
                itemsToSave.add(s);
            }
        }

        super.doWrite(hibernateTemplate, itemsToSave);

        for (Taxon t : itemsToDelete) {
            hibernateTemplate.deleteAll(itemsToDelete);
        }
    }
}
