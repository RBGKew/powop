package org.emonocot.job.dwc.exception;

import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;

/**
 *
 * @author ben
 *
 */
public class CannotFindRecordException extends TaxonProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = 3822333603663281893L;

    /**
     *
     * @param identifier the identifier of the object
     * @param title 
     */
    public CannotFindRecordException(final String identifier, final String title) {
        super("Cannot find a taxon " + title, AnnotationCode.BadIdentifier, identifier);
    }

    @Override
    public final AnnotationType getType() {
        return AnnotationType.Error;
    }

}
