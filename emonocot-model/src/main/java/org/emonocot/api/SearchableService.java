package org.emonocot.api;

import java.util.Map;

import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.pager.Page;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface SearchableService<T extends SearchableObject> extends Service<T> {
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
    * @return a Page from the resultset
    */
   Page<T> search(String query, String spatialQuery, Integer pageSize,
         Integer pageNumber, FacetName[] facets,
         Map<FacetName, Integer> selectedFacets,
         Sorting sort);
}
