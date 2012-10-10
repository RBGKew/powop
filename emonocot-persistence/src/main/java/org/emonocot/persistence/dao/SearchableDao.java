package org.emonocot.persistence.dao;

import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.api.Sorting;
import org.emonocot.model.Base;
import org.emonocot.pager.Page;

/**
 * @author ben
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
     * @param fetch
     *            Set the fetch profile
     * @return a Page from the resultset
     */
    Page<T> search(String query, String spatialQuery, Integer pageSize,
            Integer pageNumber, FacetName[] facets,
            Map<FacetName, String> selectedFacets, Sorting sort, String fetch);

    /**
     * @param example
     *            an object with fields set to the required values
     * @return a page of all matching objects
     */

    /**
     * @param example
     *            an object with fields set to the required values
     * @param ignoreCase
     *            whether to treat Uppercase/Lowercase letters the same
     * @param useLike
     *            whether to enable <i>LIKE</i> in query
     * @return a single page of objects that have the same properties as the
     *         example
     */
    Page<T> searchByExample(T example, boolean ignoreCase, boolean useLike);

}
