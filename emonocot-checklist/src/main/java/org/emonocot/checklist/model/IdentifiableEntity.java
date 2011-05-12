package org.emonocot.checklist.model;

import java.io.Serializable;

/**
 *
 * @author ben
 *
 * @param <IDENTIFIER> the class of the identifier of this object
 */
public interface IdentifiableEntity<IDENTIFIER extends Serializable> {

    /**
     *
     * @return the identifier of this entity
     */
    IDENTIFIER getIdentifier();

    /**
     *
     * @param identifier Set the identifier of this entity
     */
    void setIdentifier(IDENTIFIER identifier);
}
