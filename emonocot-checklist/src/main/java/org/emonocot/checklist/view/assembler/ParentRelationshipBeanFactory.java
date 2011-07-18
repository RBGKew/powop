package org.emonocot.checklist.view.assembler;

import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class ParentRelationshipBeanFactory extends
        TaxonRelationshipBeanFactory {

    /**
     * @return a taxon relationship term
     */
    public final TaxonRelationshipTerm getRelationshipTerm() {
        return TaxonRelationshipTerm.IS_CHILD_TAXON_OF;
    }
}
