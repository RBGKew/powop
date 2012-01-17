package org.emonocot.service.impl;

import java.util.ArrayList;

import org.emonocot.api.Service;
import org.emonocot.model.common.Base;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.dao.Dao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 * @param <T>
 *            the type of object provided by this service
 * @param <DAO>
 *            the DAO used by this service
 */
public abstract class ServiceImpl<T extends Base, DAO extends Dao<T>>
        implements Service<T> {

    /**
     *
     */
    protected DAO dao;

    /**
     * @param identifier
     *            the identifier of the object
     * @param fetch
     *            the fetch profile to use
     * @return the object
     */
    @Transactional(readOnly = true)
    public T load(String identifier, String fetch) {
        return dao.load(identifier, fetch);
    }

    /**
     *
     * @param identifier
     *            Set the identifier of the object you would like to delete
     */
    @Transactional
    public void delete(final String identifier) {
        dao.delete(identifier);
    }

    /**
     * @param identifier
     *            the identifier of the object
     * @param the
     *            fetch profile to use
     * @return the object
     */
    @Transactional(readOnly = true)
    public T find(final String identifier, final String fetch) {
        return dao.find(identifier, fetch);
    }

    /**
     * @param identifier
     *            the identifier of the object
     * @return the object loaded using the default fetch profile
     */
    @Transactional(readOnly = true)
    public T load(final String identifier) {
        return dao.load(identifier);
    }

    /**
     * @param identifier
     *            the identifier of the object
     * @return the object, or null if the object does not exist
     */
    @Transactional(readOnly = true)
    public T find(String identifier) {
        return dao.find(identifier);
    }

    /**
     * @param t
     *            the object to save
     * @return the identifier of the object
     */
    @Transactional
    public T save(T t) {
        return dao.save(t);
    }

    /**
     * @param t
     *            the object to save
     */
    @Transactional
    public void saveOrUpdate(T t) {
        dao.saveOrUpdate(t);
    }

    /**
     * @param t
     *            the object to update
     */
    @Transactional
    public void update(T t) {
        dao.update(t);
    }

    /**
     * @param t
     *            the object to merge
     * @return the merged object
     */
    @Transactional
    public final T merge(final T t) {
        return dao.merge(t);
    }

    /**
     * @param page Set the offset (in size chunks, 0-based), optional
     * @param size Set the page size
     * @return A page of results
     */
    @Transactional
    public final Page<T> list(final Integer page, final Integer size) {
        Long numberOfResults = dao.count();
        if (numberOfResults == 0) {
            return new DefaultPageImpl<T>(numberOfResults.intValue(), page,
                    size, new ArrayList<T>());
        } else {
            return new DefaultPageImpl<T>(numberOfResults.intValue(), page,
                    size, dao.list(page, size));
        }
    }
}
