package org.emonocot.job.dwc.taxon;

import org.emonocot.model.common.AnnotationType;

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
     * @param msg Set the message
     */
    public TaxonAlreadyProcessedException(final String msg) {
        super(msg);
    }

    /**
     * @return the annotation type
     */
    @Override
    public final AnnotationType getType() {
        return AnnotationType.Error;
    }

}
