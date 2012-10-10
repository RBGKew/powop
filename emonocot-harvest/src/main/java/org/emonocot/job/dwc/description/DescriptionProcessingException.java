package org.emonocot.job.dwc.description;

import org.emonocot.job.dwc.DarwinCoreProcessingException;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;

/**
 *
 * @author ben
 *
 */
public class DescriptionProcessingException extends
        DarwinCoreProcessingException {

    /**
     * @param code the annotation code
     * @param value the value
     * @param msg the message
     */
    public DescriptionProcessingException(final String msg,
            final AnnotationCode code, final String value) {
        super(msg, code, RecordType.TextContent, value);
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
