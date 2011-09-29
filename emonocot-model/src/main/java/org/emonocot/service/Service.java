package org.emonocot.service;

import java.util.Map;

import org.emonocot.model.common.Base;
import org.emonocot.model.pager.Page;

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

   /**
    *
    * @param identifier
    *            Set the identifier of the object you would like to delete
    */
   void delete(String identifier);

    /**
     *
     * @param identifier The identifier of the object required.
     * @return The object, or null if the object is not found
     */
    T find(String identifier);

    /**
    *
    * @param identifier
    *            Set the identifier of the object you would like to retrieve
    * @param fetch Set the fetch profile to use
    * @return the object or throw and exception if that object does not exist
    */
   T load(String identifier, String fetch);

  /**
   *
   * @param identifier
   *            Set the identifier of the object you would like to retrieve
   * @param fetch Set the fetch profile to use
   * @return the object or null if that object does not exist
   */
   T find(String identifier, String fetch);

   /**
    *
    * @param t The object to save.
    * @return the id of the object
    */
   T save(T t);

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
         String sort);
}
