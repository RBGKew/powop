package org.emonocot.checklist.model;

import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface ChangeEvent<T extends IdentifiableEntity> {

    /**
     *
     * @return the object
     */
    T getObject();

    /**
     *
     * @return the type of change
     */
    ChangeType getType();

    /**
     *
     * @return the datestamp
     */
    DateTime getDatestamp();

    /**
     *
     * @return the identifier
     */
    String getIdentifier();
}
