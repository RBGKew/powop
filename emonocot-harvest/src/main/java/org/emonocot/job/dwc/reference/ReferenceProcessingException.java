package org.emonocot.job.dwc.reference;

import org.emonocot.job.dwc.DarwinCoreProcessingException;
import org.emonocot.model.common.AnnotationType;

/**
 *
 * @author ben
 *
 */
public class ReferenceProcessingException extends
        DarwinCoreProcessingException {

    /**
     *
     * @param msg the message
     */
    public ReferenceProcessingException(final String msg) {
        super(msg);
    }

    /**
     *
     */
    private static final long serialVersionUID = 999609749452018246L;

    @Override
    public final AnnotationType getType() {
        return AnnotationType.Error;
    }
}
