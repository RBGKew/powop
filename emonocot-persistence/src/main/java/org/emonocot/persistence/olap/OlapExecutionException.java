package org.emonocot.persistence.olap;

import org.springframework.dao.DataAccessException;

/**
 *
 * @author ben
 *
 */
public class OlapExecutionException extends DataAccessException {

    /**
     *
     */
    private static final long serialVersionUID = -324685026830301627L;

    /**
     *
     * @param msg Set the message
     * @param cause Set the cause
     */
    public OlapExecutionException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
