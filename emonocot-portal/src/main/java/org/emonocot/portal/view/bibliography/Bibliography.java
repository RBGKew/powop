package org.emonocot.portal.view.bibliography;

import java.util.List;

import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;

/**
 *
 * @author ben
 *
 */
public interface Bibliography {

    /**
     *
     * @param taxon Set the taxon
     */
    void setReferences(final Taxon taxon);

    /**
     *
     * @param refernce Set the reference
     * @return A string key which can be displayed in the taxon page as a
     *         citation reference to the reference
     */
    String getKey(Reference refernce);

    /**
     *
     * @return a sorted list of references
     */
    List<Reference> getReferences();

}
