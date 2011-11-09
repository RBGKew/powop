package org.emonocot.job.dwc.taxon;

import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public class NoIdentifierException extends TaxonProcessingException {

   /**
    *
    */
   private static final long serialVersionUID = -4140158822002083763L;

    /**
     *
     * @param t
     *            the taxon
     */
    public NoIdentifierException(final Taxon t) {
        super(t + " has no identifier", AnnotationCode.BadField, "identifier");
    }

    @Override
    public final AnnotationType getType() {
        return AnnotationType.Error;
    }
}
