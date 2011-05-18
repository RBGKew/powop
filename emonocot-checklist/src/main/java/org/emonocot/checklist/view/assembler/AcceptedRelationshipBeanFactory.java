package org.emonocot.checklist.view.assembler;

import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class AcceptedRelationshipBeanFactory extends
        TaxonRelationshipBeanFactory {

    /**
     * @return a taxon relationship term
     */
    public final TaxonRelationshipTerm getRelationshipTerm() {
        return TaxonRelationshipTerm.HAS_SYNONYM;
    }
}
