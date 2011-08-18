package org.emonocot.job.dwc.image;

import org.emonocot.job.dwc.DarwinCoreProcessingException;

/**
 *
 * @author ben
 *
 */
public class ImageProcessingException extends
        DarwinCoreProcessingException {

    /**
     *
     * @param msg the message
     */
    public ImageProcessingException(final String msg) {
        super(msg);
    }

    /**
     *
     */
    private static final long serialVersionUID = 999609749452018246L;
}
