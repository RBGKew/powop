package org.openarchives.pmh;

/**
 *
 * @author ben
 *
 */
public class CannotDisseminateFormatException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -7439206216525633443L;

    /**
     *
     * @param message The message to be displayed
     */
    public CannotDisseminateFormatException(final String message) {
        super(message);
    }
}
