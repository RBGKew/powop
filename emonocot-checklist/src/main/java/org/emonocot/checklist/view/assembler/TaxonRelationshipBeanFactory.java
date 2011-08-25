package org.emonocot.checklist.view.assembler;

import java.net.URI;
import java.net.URISyntaxException;

import org.dozer.BeanFactory;
import org.emonocot.checklist.model.Taxon;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tdwg.voc.Relationship;
import org.tdwg.voc.TaxonRelationshipTerm;
import org.tdwg.voc.ToTaxon;

/**
 *
 * @author ben
 *
 */
public abstract class TaxonRelationshipBeanFactory implements BeanFactory {
    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(TaxonRelationshipBeanFactory.class);

    /**
     *
     * @return a taxon relationship term
     */
    public abstract TaxonRelationshipTerm getRelationshipTerm();

    /**
     *
     * @param source Set the source bean
     * @param sourceClass Set the class of the source bean
     * @param targetBeanId Set the target bean id
     * @return a new object
     */
    public final Object createBean(final Object source, final Class sourceClass,
            final String targetBeanId) {
        if (source == null) {
            return null;
        }
        Relationship relationship = new Relationship();
        relationship.setRelationshipCategoryRelation(getRelationshipTerm());
        try {
            Taxon taxon = (Taxon) source;
            ToTaxon toTaxon = new ToTaxon();
            toTaxon.setResource(new URI(taxon.getIdentifier()));
            relationship.setToTaxon(toTaxon);
        } catch (LazyInitializationException lie) {
            logger.error("Exception initializing taxon " + lie.getMessage());
        } catch (URISyntaxException use) {
            logger.error("Could not convert identifier to uri "
                    + use.getMessage());
        }
        return relationship;
    }

}
