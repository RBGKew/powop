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
 * @param <T>
 */
public abstract class ServiceImpl<T extends Base, DAO extends Dao<T>> implements Service<T> {
    
    public T load(String identifier, String fetch) {
        return dao.load(identifier, fetch);
    }

    public T find(String identifier, String fetch) {
        return dao.find(identifier, fetch);
    }

    protected DAO dao;

    @Transactional(readOnly = true)
    public final T load(final String identifier) {
        return dao.load(identifier);
    }

    @Transactional(readOnly = true)
    public T find(String identifier) {
        return dao.find(identifier);
    }

    @Transactional
    public Long save(T t) {
        return dao.save(t);
    }
    
    @Transactional(readOnly = true)
    public Page<T> search(String query, String spatialQuery, Integer pageSize,
            Integer pageNumber, FacetName[] facets,
            Map<FacetName, Integer> selectedFacets) {
        return dao.search(query, spatialQuery, pageSize, pageNumber, facets, selectedFacets);
    }
}
