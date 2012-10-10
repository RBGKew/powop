package org.emonocot.job.oaipmh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.orm.hibernate3.HibernateOperations;

/**
 * @author ben
 */
public class Writer extends HibernateItemWriter<Taxon> {

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(Writer.class);

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
            if (t.getNamePublishedIn() != null) {
                for (Taxon t1 : t.getNamePublishedIn().getTaxa()) {
                    if (t1.getIdentifier().equals(t.getIdentifier())) {
                        t.getNamePublishedIn().getTaxa().remove(t1);
                    }
                }
                t.setNamePublishedIn(null);
            }
            if (!t.getReferences().isEmpty()) {
                for (Reference r : t.getReferences()) {
                    Taxon theTaxon = null;
                    for (Taxon t1 : r.getTaxa()) {
                        if (t1.getIdentifier().equals(t.getIdentifier())) {
                            theTaxon = t1;
                            break;
                        }
                    }
                    if (theTaxon != null) {
                        r.getTaxa().remove(theTaxon);
                    }
                }
            }
            if (!t.getImages().isEmpty()) {
                for (Image i : t.getImages()) {
                    Taxon theTaxon = null;
                    for (Taxon t1 : i.getTaxa()) {
                        if (t1.getIdentifier().equals(t.getIdentifier())) {
                            theTaxon = t1;
                            break;
                        }
                    }
                    if (theTaxon != null) {
                        i.getTaxa().remove(theTaxon);
                    }
                    if (i.getTaxon() != null
                            && i.getTaxon().getIdentifier()
                                    .equals(t.getIdentifier())) {
                        i.setTaxon(null);

                    }
                }
            }
            if (t.getParentNameUsage() != null) {
                for (Taxon child : t.getParentNameUsage().getChildNameUsages()) {
                    if (child.getIdentifier().equals(t.getIdentifier())) {
                        logger.debug(t.getIdentifier()
                                + " removed successfully from parent? "
                                + t.getParentNameUsage().getChildNameUsages().remove(child));
                        break;
                    }
                }

                t.setParentNameUsage(null);
            }
            if (t.getAcceptedNameUsage() != null) {
                for (Taxon synonym : t.getAcceptedNameUsage().getSynonymNameUsages()) {
                    if (synonym.getIdentifier().equals(t.getIdentifier())) {
                        logger.debug(t.getIdentifier()
                                + " removed successfully from accepted name? "
                                + t.getAcceptedNameUsage().getSynonymNameUsages().remove(synonym));
                        break;
                    }
                }
                t.setAcceptedNameUsage(null);
            }
            if (!t.getChildNameUsages().isEmpty()) {
                for (Taxon child : t.getChildNameUsages()) {
                    child.setParentNameUsage(null);
                }
                t.getChildNameUsages().clear();
            }
            if (!t.getSynonymNameUsages().isEmpty()) {
                for (Taxon synonym : t.getSynonymNameUsages()) {
                    synonym.setAcceptedNameUsage(null);
                }
                t.getSynonymNameUsages().clear();
            }
        }
        List<Taxon> toSave = new ArrayList<Taxon>(itemsToSave.values());
        super.doWrite(hibernateTemplate, toSave);

        hibernateTemplate.deleteAll(itemsToDelete.values());
        hibernateTemplate.flush();
    }
}
