package org.emonocot.pager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.RangeFacet;

/**
 * Abstract class that represents a single page in a set of objects returned
 * from a query (possibly a subset of the total number of matching objects
 * available).
 *
 * NOTE: Indices for objects and pages are 0-based.
 *
 * @author ben
 *
 * @param <T>
 */
public interface Page<T> {
    /**
     * The total number of pages available for this query, or 0 if there are no
     * matching objects.
     *
     * @return the number of pages available
     */
    Integer getPagesAvailable();

    /**
     * The index of the next page in this result set, or null if this is the
     * last page in the result set.
     *
     * @return the index of the next page
     */
    Integer getNextIndex();

    /**
     * The index of the previous page in this result set, or null if this is the
     * first page in the result set.
     *
     * @return the index of the previous page
     */
    Integer getPrevIndex();

    /**
     * The index of this page.
     *
     * @return the index of this page
     */
    Integer getCurrentIndex();

    /**
     * Gets the size of pages in this result set. Can be null if all matching
     * objects should be returned.
     *
     * @return The page size
     */
    Integer getPageSize();

    /**
     * Get the total number of objects in this result set (not in this page). If
     * count > {@link #getPageSize()} then {@link #getPagesAvailable()} > 1.
     *
     * @return the total number of objects available.
     */
    Integer getSize();

    /**
     * Returns the index of the first record in this result set.
     *
     * @return the index of the first record in this result set
     */
    Integer getFirstRecord();

    /**
     * Returns the index of the last record in this result set.
     *
     * @return the index of the last record in this result set
     */
    Integer getLastRecord();

    /**
     * Returns the records in this page.
     *
     * @return the records in this page
     */
    List<T> getRecords();

    /**
     *
     * @return a map of facet lists organised by the name of the facet
     */
    FacetField getFacetField(String facetName);
    
    RangeFacet getRangeFacet(String facetName);

   /**
    *
    * @return a list of the names of available facets
    */
   List<String> getFacetNames();

    /**
     *
     * @param name Set the name of the parameter
     * @param value Set the value of the parameter
     */
    void putParam(String name, Object value);

    /**
    *
    * @return the parameters
    */
   Map<String, Object> getParams();

   /**
    *
    * @return the parameter names
    */
   Set<String> getParamNames();

    /**
     * Get a string label for a given page (NOTE: Labels may not be calculated
     * for each page in the result set, especially if the result set is large or
     * the operation for calculating the label is expensive. The indices of the
     * pages for which labels are available are given by {@link #getIndices()}.
     *
     * @param index The index of the page you want a label for
     * @return A label for the page indicated or null if this pager has not
     *         calculated a label for that page
     */
   String getPageNumber(int index);
   /**
    * Return the current page number
    */
   String getCurrentPageNumber();
   

    /**
     * Get a list of page indices for which labels are available.
     *
     * @return A list of indices
     */
   List<Integer> getIndices();

  /**
   *
   * @return a list of the names of selected facets
   */
  Set<String> getSelectedFacetNames();

    /**
     *
     * @return The selected facets
     */
  Map<String, String> getSelectedFacets();

  /**
   *
   * @param facetName Set the facet name
   * @return true if the facet is selected, false otherwise
   */
  boolean isFacetSelected(String facetName);

  /**
   * @param facetName Set the facet name
   * @param selected Set the selected facet
   */
  void setSelectedFacets(Map<String,String> selectedFacets);

  /**
   *
   * @return the sorting
   */
  String getSort();

  /**
   * @param newSort set the sorting
   */
  void setSort(String newSort);
}
