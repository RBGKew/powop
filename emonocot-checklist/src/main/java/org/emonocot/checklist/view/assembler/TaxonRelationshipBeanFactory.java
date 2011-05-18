package org.emonocot.checklist.view.assembler;

import org.dozer.BeanFactory;
import org.tdwg.voc.Relationship;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public abstract class TaxonRelationshipBeanFactory implements BeanFactory {

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
        return relationship;
    }

}
