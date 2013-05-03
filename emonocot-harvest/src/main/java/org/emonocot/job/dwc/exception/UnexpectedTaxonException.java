package org.emonocot.job.dwc.exception;

import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;

/**
 *
 * @author ben
 *
 */
public class UnexpectedTaxonException extends TaxonProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = -79759942758744782L;

    /**
     *
     * @param taxon the unexpected taxon
     */
    public UnexpectedTaxonException(final Taxon taxon) {
        super(
                taxon.toString() + " was not expected", AnnotationCode.Unexpected,
                taxon.getIdentifier());
    }

    @Override
    public final AnnotationType getType() {
        return AnnotationType.Warn;
    }

}
