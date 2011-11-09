package org.emonocot.job.dwc.description;

import org.emonocot.model.common.AnnotationCode;

/**
 *
 * @author ben
 *
 */
public class NoFeatureException extends DescriptionProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = 3691834819729168275L;

    /**
     *
     * @param msg Set the message
     */
    public NoFeatureException(final String msg) {
        super(msg, AnnotationCode.BadField, "feature");
    }

}
