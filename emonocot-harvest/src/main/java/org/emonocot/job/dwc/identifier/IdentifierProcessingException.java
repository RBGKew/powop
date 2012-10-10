package org.emonocot.job.dwc.identifier;

import org.emonocot.job.dwc.DarwinCoreProcessingException;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;

/**
 *
 * @author ben
 *
 */
public class IdentifierProcessingException extends
        DarwinCoreProcessingException {

    /**
     *
     * @param msg the message
     * @param code the code
     * @param value the value
     */
    public IdentifierProcessingException(final String msg,
            final AnnotationCode code, final String value) {
        super(msg, code, RecordType.Identifier, value);
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
