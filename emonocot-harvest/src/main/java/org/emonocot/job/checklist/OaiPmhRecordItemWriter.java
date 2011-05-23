package org.emonocot.job.checklist;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.orm.hibernate3.HibernateOperations;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordItemWriter extends HibernateItemWriter<Taxon> {

    @Override
    protected final void doWrite(final HibernateOperations hibernateTemplate,
            final List<? extends Taxon> items) {
        List<Taxon> itemsToDelete = new ArrayList<Taxon>();
        for (Taxon t : items) {
            if (t.isDeleted()) {
                itemsToDelete.add(t);
            }
        }

        for (Taxon t : itemsToDelete) {
            if (items.contains(t)) {
                items.remove(t);
            }
        }

        super.doWrite(hibernateTemplate, items);

        for (Taxon t : itemsToDelete) {
            hibernateTemplate.deleteAll(itemsToDelete);
        }
    }
}
