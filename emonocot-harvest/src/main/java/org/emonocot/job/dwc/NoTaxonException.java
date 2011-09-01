package org.emonocot.job.dwc;

import org.emonocot.model.common.AnnotationType;

/**
 *
 * @author ben
 *
 */
public class NoTaxonException extends DarwinCoreProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = 4236165074026471554L;

    /**
     *
     * @param msg Set the message
     */
    public NoTaxonException(final String msg) {
        super(msg);
    }

    @Override
    public final AnnotationType getType() {
        return AnnotationType.Error;
    }

}
