package org.emonocot.job.oaipmh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
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
            if (t.getProtologue() != null) {
                for (Taxon t1 : t.getProtologue().getTaxa()) {
                    if (t1.getIdentifier().equals(t.getIdentifier())) {
                        t.getProtologue().getTaxa().remove(t1);
                    }
                }
                t.setProtologue(null);
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
            if (t.getParent() != null) {
                for (Taxon child : t.getParent().getChildren()) {
                    if (child.getIdentifier().equals(t.getIdentifier())) {
                        logger.debug(t.getIdentifier()
                                + " removed successfully from parent? "
                                + t.getParent().getChildren().remove(child));
                        break;
                    }
                }

                t.setParent(null);
            }
            if (t.getAccepted() != null) {
                for (Taxon synonym : t.getAccepted().getSynonyms()) {
                    if (synonym.getIdentifier().equals(t.getIdentifier())) {
                        logger.debug(t.getIdentifier()
                                + " removed successfully from accepted name? "
                                + t.getAccepted().getSynonyms().remove(synonym));
                        break;
                    }
                }
                t.setAccepted(null);
            }
            if (!t.getChildren().isEmpty()) {
                for (Taxon child : t.getChildren()) {
                    child.setParent(null);
                }
                t.getChildren().clear();
            }
            if (!t.getSynonyms().isEmpty()) {
                for (Taxon synonym : t.getSynonyms()) {
                    synonym.setAccepted(null);
                }
                t.getSynonyms().clear();
            }
        }
        List<Taxon> toSave = new ArrayList<Taxon>(itemsToSave.values());
        super.doWrite(hibernateTemplate, toSave);

        hibernateTemplate.deleteAll(itemsToDelete.values());
        hibernateTemplate.flush();
    }
}
