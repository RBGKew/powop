package org.emonocot.checklist.persistence.pager;

import java.util.List;

/**
 * Abstract class that represents a single page in a set of objects 
 * returned from a query (possibly a subset of the total number of matching objects
 * available).
 * 
 * NOTE: Indices for objects and pages are 0-based.
 * @author ben
 *
 * @param <T>
 */
public interface Page<T> {
	/**
	 * The total number of pages available for this query, or 0 if there are 
	 * no matching objects
	 * 
	 * @return The number of pages available
	 */
    public Integer getPagesAvailable();
	
    /**
     * The index of the next page in this result set, or null if this is the
     * last page in the result set. 
     * @return The index of the next page
     */
	public Integer getNextIndex();
	
	/**
     * The index of the previous page in this result set, or null if this is the
     * first page in the result set. 
     * @return The index of the previous page
     */
	public Integer getPrevIndex();
	
	/**
	 * The index of this page
	 * @return The index of this page
	 */
	public Integer getCurrentIndex();
	
	/**
	 * Gets the size of pages in this result set. Can be null if all matching 
	 * objects should be returned.
	 * @return The page size
	 */
	public Integer getPageSize();
	
	/**
	 * Get the total number of objects in this result set (not in this page). 
	 * If count > {@link #getPageSize()} then {@link #getPagesAvailable()} > 1
	 * @return the total number of objects available.
	 */
	public Integer size();

	/**
	 * Returns the index of the first record in this result set
	 * @return
	 */
	public Integer getFirstRecord();

	/**
	 * Returns the index of the last record in this result set
	 * @return
	 */
	public Integer getLastRecord();
	
	/**
	 * Returns the records in this page.
	 * @return
	 */
	public List<T> getRecords();
}

