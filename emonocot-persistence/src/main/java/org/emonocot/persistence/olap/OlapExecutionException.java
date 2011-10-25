package org.emonocot.persistence.olap;

import org.springframework.dao.DataAccessException;

public class OlapExecutionException extends DataAccessException {

    /**
     * 
     */
    private static final long serialVersionUID = -324685026830301627L;

    public OlapExecutionException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
