package org.emonocot.service.impl;

import java.util.Map;

import org.emonocot.model.common.Base;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.dao.Dao;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.service.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 * @param <T> the type of object provided by this service
 * @param <DAO> the DAO used by this service
 */
public abstract class ServiceImpl<T extends Base, DAO extends Dao<T>>
        implements Service<T> {

    /**
     * @param identifier the identifier of the object
     * @param the fetch profile to use
     * @return the object
     */
    public T load(String identifier, String fetch) {
        return dao.load(identifier, fetch);
    }

    /**
     * @param identifier the identifier of the object
     * @param the fetch profile to use
     * @return the object
     */
    public T find(String identifier, String fetch) {
        return dao.find(identifier, fetch);
    }

    /**
     *
     */
    protected DAO dao;

    /**
     * @param identifier the identifier of the object
     * @return the object loaded using the default fetch profile
     */
    @Transactional(readOnly = true)
    public final T load(final String identifier) {
        return dao.load(identifier);
    }

    /**
     * @param identifier the identifier of the object
     * @return the object, or null if the object does not exist
     */
    @Transactional(readOnly = true)
    public T find(String identifier) {
        return dao.find(identifier);
    }

    /**
     * @param t the object to save
     * @return the identifier of the object
     */
    @Transactional
    public Long save(T t) {
        return dao.save(t);
    }

    /**
     * @param query Set the lucene query
     * @param spatialQuery Set the spatial query
     * @param pageSize Set the maximum number of objects to return
     * @param pageNumber Set the offset (in 'pageNumber' chunks) from the start of the resultset (0-based!)
     * @param facets Set the facets to calculate
     * @param selectedFacets Set the facets to select, restricing the results of the query
     * @return a page of results
     */
    @Transactional(readOnly = true)
    public Page<T> search(String query, String spatialQuery, Integer pageSize,
            Integer pageNumber, FacetName[] facets,
            Map<FacetName, Integer> selectedFacets) {
        return dao.search(query, spatialQuery, pageSize, pageNumber, facets,
                selectedFacets);
    }
}
