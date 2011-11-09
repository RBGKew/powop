package org.emonocot.job.dwc.taxon;

import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.taxon.Taxon;

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
        super("Taxon " + taxon.getIdentifier()
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
