package org.emonocot.checklist.view.assembler;

import org.dozer.BeanFactory;
import org.emonocot.checklist.model.Family;
import org.emonocot.checklist.model.Rank;
import org.emonocot.checklist.model.Taxon;
import org.tdwg.voc.TaxonConcept;

/**
 *
 * @author ben
 *
 */
public class TaxonBeanFactory implements BeanFactory {

   /**
    *
    * @param source Set the source bean
    * @param sourceClass Set the class of the source bean
    * @param targetBeanId Set the target bean id
    * @return a new object
    */
    public final Object createBean(final Object source,
            final Class<?> sourceClass, final String targetBeanId) {
        if (source == null) {
            return null;
        }
        TaxonConcept taxonConcept = new TaxonConcept();
        Taxon taxon  = (Taxon) source;
        if (taxon.getAcceptedName() == null
                || taxon.getAcceptedName().getId().equals(taxon.getId())) {
            // This taxon is accepted
            // Due to the fact that family records are not present, a dummy
            // reference to the parent taxon must
            // be added for accepted genera
            if (taxon.getParentTaxon() == null
                    && (taxon.getRank() != null && taxon.getRank().equals(
                            Rank.GENUS))) {
                Taxon parent = new Taxon();
                Family family = Family.valueOf(taxon.getFamily());
                parent.setName(taxon.getFamily());
                parent.setRank(Rank.FAMILY);
                parent.setIdentifier(family.getIdentifier());
                taxon.setParentTaxon(parent);
            }
        }
        return taxonConcept;
    }

}
