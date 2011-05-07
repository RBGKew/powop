package org.emonocot.service;

import org.emonocot.model.common.Base;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface Service<T extends Base> {
    /**
     *
     * @param identifer The identifier of the object required.
     * @return The object, or throw a
     *         HibernateObjectRetrievalFailureException if the
     *         object is not found
     */
    T load(String identifer);
}
