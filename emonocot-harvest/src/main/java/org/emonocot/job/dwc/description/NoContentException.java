package org.emonocot.job.dwc.description;

import org.emonocot.model.constants.AnnotationCode;

/**
 *
 * @author ben
 *
 */
public class NoContentException extends DescriptionProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = 3691834819729168275L;

    /**
     *
     * @param msg Set the message
     */
    public NoContentException(final String msg) {
        super(msg, AnnotationCode.BadField, "content");
    }

}
