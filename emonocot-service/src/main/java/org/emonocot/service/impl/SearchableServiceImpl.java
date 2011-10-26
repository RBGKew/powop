package org.emonocot.service.impl;

import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.api.SearchableService;
import org.emonocot.api.Sorting;
import org.emonocot.model.common.Base;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.dao.SearchableDao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 * @param <T> the type of object provided by this service
 * @param <DAO> the DAO used by this service
 */
public abstract class SearchableServiceImpl<T extends Base, DAO extends SearchableDao<T>>
        extends ServiceImpl<T,DAO> implements SearchableService<T> {

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
            Map<FacetName, Integer> selectedFacets, Sorting sort) {
        return dao.search(query, spatialQuery, pageSize, pageNumber, facets,
                selectedFacets, sort);
    }
}
