package org.emonocot.ws.checklist;

import org.openarchives.pmh.Error;
import org.openarchives.pmh.ErrorCode;

/**
 *
 * @author ben
 *
 */
public class OaiPmhException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 7734870366319675757L;

    /**
     *
     */
    private Error error;

    /**
     *
     * @param newError Set the error
     */
    public OaiPmhException(final Error newError) {
        super(newError.getValue());
        this.error = newError;
    }

    /**
     *
     * @param string Set the error
     */
    public OaiPmhException(final String string) {
        super(string);
    }

    /**
     * @return the error code
     */
    public final ErrorCode getCode() {
        return error.getCode();
    }
}
