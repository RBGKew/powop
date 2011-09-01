package org.emonocot.job.dwc.taxon;

import org.emonocot.model.common.AnnotationType;

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
     * @param identifier the identifier of the unexpected taxon
     */
    public UnexpectedTaxonException(final String identifier) {
        super("Found unexpected taxon with identifier " + identifier);
    }

    @Override
    public final AnnotationType getType() {
        return AnnotationType.Unexpected;
    }

}
