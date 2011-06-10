package org.emonocot.model.pager;

import java.util.List;

import org.hibernate.search.query.facet.Facet;

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
    Integer size();

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
     * Add a list of facets under a particular name.
     *
     * @param facetName the name of the facet
     * @param facets Set the facets
     */
    void addFacets(String facetName, List<Facet> facets);

    /**
     *
     * @param facetName The name of the facets you're interested in
     * @return a list of facets
     */
    List<Facet> getFacets(String facetName);
}
