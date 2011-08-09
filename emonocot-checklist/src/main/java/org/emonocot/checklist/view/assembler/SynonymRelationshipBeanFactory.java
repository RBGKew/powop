package org.emonocot.checklist.view.assembler;

import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class SynonymRelationshipBeanFactory extends
        TaxonRelationshipBeanFactory {

    /**
     * @return the taxon relationship term
     */
    public final TaxonRelationshipTerm getRelationshipTerm() {
        return TaxonRelationshipTerm.IS_SYNONYM_FOR;
    }
}
