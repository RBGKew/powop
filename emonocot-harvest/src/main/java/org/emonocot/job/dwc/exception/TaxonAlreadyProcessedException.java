package org.emonocot.job.dwc.exception;

import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;

/**
 *
 * @author ben
 *
 */
public class TaxonAlreadyProcessedException extends TaxonProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @param taxon Set the taxon
     */
    public TaxonAlreadyProcessedException(final Taxon taxon) {
        super("Taxon " + taxon.toString()
                + " already found once in this archive",
                AnnotationCode.AlreadyProcessed, taxon.getIdentifier());
    }

    /**
     * @return the annotation type
     */
    @Override
    public final AnnotationType getType() {
        return AnnotationType.Error;
    }

}
