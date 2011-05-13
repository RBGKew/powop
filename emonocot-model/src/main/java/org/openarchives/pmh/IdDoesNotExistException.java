package org.openarchives.pmh;

import java.io.Serializable;

/**
 *
 * @author ben
 *
 */
public class IdDoesNotExistException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 7918907769810176520L;

    /**
     *
     */
    private Serializable identifier;

    /**
     *
     * @param newIdentifier Set the identifier which does not exist
     */
    public IdDoesNotExistException(final Serializable newIdentifier) {
        this.identifier = newIdentifier;
    }

    @Override
    public final String getMessage() {
        return "Could not find object with identifier " + identifier;
    }

    /**
     *
     * @return the identifier which does not exist
     */
    public final Serializable getIdentifier() {
        return identifier;
    }
}
