package org.emonocot.checklist.persistence;

import java.io.Serializable;

import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.model.pager.Page;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface IdentifiableService<T extends IdentifiableEntity> {

    /**
     * Find an object with the given identifier.
     *
     * @param identifier Set the identifier of the object to find
     * @return a change event
     */
    ChangeEvent<T> find(Serializable identifier);

    /**
     *
     * @param set
     *            The set (or null if the results are from all sets)
     * @param from
     *            The date time after which the events in the result set must
     *            have occured
     * @param until
     *            The date time before which the events in the result set must
     *            have occured
     * @param pageSize
     *            The maximum number of results returned
     * @param pageNumber
     *            The offset (in pageSize chunks) from the beginning of the
     *            results
     * @return A pager of change events
     */
    Page<ChangeEvent<T>> page(String set, DateTime from, DateTime until,
            Integer pageSize, Integer pageNumber);

}
