package org.emonocot.checklist.model;

import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public class ChangeEventImpl<T extends IdentifiableEntity> implements
        ChangeEvent<T> {
    /**
     *
     */
    private T object;

    /**
     *
     */
    private ChangeType type;

    /**
     *
     */
    private DateTime date;

    /**
     *
     * @param newObject Set the object
     * @param newType Set the change type
     * @param newDate Set the date
     */
    public ChangeEventImpl(final T newObject, final ChangeType newType,
            final DateTime newDate) {
        this.object = newObject;
        this.type = newType;
        this.date = newDate;
    }

    @Override
    public final T getObject() {
        return object;
    }

    @Override
    public final ChangeType getType() {
        return type;
    }

    @Override
    public final DateTime getDatestamp() {
        return date;
    }

    @Override
    public final String getIdentifier() {
        return object.getIdentifier().toString();
    }
}
