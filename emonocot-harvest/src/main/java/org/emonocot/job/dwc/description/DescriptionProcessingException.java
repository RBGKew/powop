package org.emonocot.job.dwc.description;

import org.emonocot.job.dwc.DarwinCoreProcessingException;

/**
 *
 * @author ben
 *
 */
public class DescriptionProcessingException extends
        DarwinCoreProcessingException {

    /**
     *
     * @param msg the message
     */
    public DescriptionProcessingException(final String msg) {
        super(msg);
    }

    /**
     *
     */
    private static final long serialVersionUID = 999609749452018246L;
}
