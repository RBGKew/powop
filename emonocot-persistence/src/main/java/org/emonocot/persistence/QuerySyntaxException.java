package org.emonocot.persistence;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 *
 * @author ben
 *
 */
public class QuerySyntaxException extends InvalidDataAccessApiUsageException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @param msg the message
     */
    public QuerySyntaxException(final String msg) {
        super(msg);
    }

    /**
     *
     * @param msg the message
     * @param cause the cause of the exception
     */
    public QuerySyntaxException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
