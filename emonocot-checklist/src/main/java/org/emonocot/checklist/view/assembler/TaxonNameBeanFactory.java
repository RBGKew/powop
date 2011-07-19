package org.emonocot.checklist.view.assembler;

import org.dozer.BeanFactory;
import org.emonocot.checklist.model.Taxon;
import org.tdwg.voc.TaxonName;

/**
 *
 * @author ben
 *
 */
public class TaxonNameBeanFactory implements BeanFactory {

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

        Taxon taxon = (Taxon) source;
        TaxonName taxonName = new TaxonName();
        if (taxon.getSpecies() != null) {
            taxonName.setGenusPart(taxon.getGenus());
        } else {
            taxonName.setUninomial(taxon.getGenus());
        }
        return taxonName;
    }

}
