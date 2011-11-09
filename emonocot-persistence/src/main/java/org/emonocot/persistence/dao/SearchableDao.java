package org.emonocot.persistence.dao;

import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.api.Sorting;
import org.emonocot.model.common.Base;
import org.emonocot.model.pager.Page;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface SearchableDao<T extends Base> extends Dao<T> {

    /**
     * @param query
     *            A lucene query
     * @param spatialQuery
     *            A spatial query to filter the results by
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param facets
     *            The names of the facets you want to calculate
     * @param selectedFacets
     *            A map of facets which you would like to restrict the search by
     * @param sort
     *            A representation for the order results should be returned in
     * @param fetch Set the fetch profile
     * @return a Page from the resultset
     */
  Page<T> search(String query, String spatialQuery, Integer pageSize,
          Integer pageNumber, FacetName[] facets,
          Map<FacetName, String> selectedFacets, Sorting sort, String fetch);

}
