package org.emonocot.model.pager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.search.query.facet.Facet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public abstract class AbstractPageImpl<T> implements Page<T>, Serializable {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(AbstractPageImpl.class);

    /**
     *
     */
    private Integer pagesAvailable;

    /**
     *
     */
    private Integer prevIndex;

    /**
     *
     */
    private Integer nextIndex;

    /**
     *
     */
    private Integer currentIndex;

    /**
     *
     */
    private Integer firstRecord;

    /**
     *
     */
    private Integer lastRecord;

    /**
     *
     */
    private Integer size;

    /**
     *
     */
    private List<T> records;

    /**
     *
     */
    private Integer pageSize;

    /**
     *
     */
    private Map<String, List<Facet>> facets
        = new HashMap<String, List<Facet>>();

    /**
     * Constructor.
     *
     * @param newCurrentIndex
     *            the page of this result set (0-based), can be null
     * @param count
     *            the total number of results available for this query
     * @param newPageSize
     *            The size of pages (can be null if all results should be
     *            returned if available)
     * @param newRecords
     *            A list of objects in this page (can be empty if there were no
     *            results)
     */
    public AbstractPageImpl(final Integer count, final Integer newCurrentIndex,
            final Integer newPageSize, final List<T> newRecords) {
        if (newCurrentIndex != null) {
            this.currentIndex = newCurrentIndex;
        } else {
            this.currentIndex = 0;
        }

        this.pageSize = newPageSize;
        if (count == 0) {
            pagesAvailable = 1;
        } else if (newPageSize != null) {
            if (0 == count % newPageSize) {
                pagesAvailable = count / newPageSize;
            } else {
                pagesAvailable = (count / newPageSize) + 1;
            }
        } else {
            pagesAvailable = 1;
        }

        if (pagesAvailable == 1) {
            nextIndex = null;
            prevIndex = null;
        } else {
            if (0 < this.currentIndex) {
                prevIndex = this.currentIndex - 1;
            }
            if (this.currentIndex < (pagesAvailable - 1)) {
                nextIndex = this.currentIndex + 1;
            }
        }
        if (newPageSize == null) {
            this.firstRecord = 1;
            this.lastRecord = newRecords.size();
        } else {
            this.firstRecord = (this.currentIndex * newPageSize) + 1;
            this.lastRecord = (this.currentIndex * newPageSize)
                               + newRecords.size();
        }

        this.size = count;
        this.records = newRecords;
    }

    /**
     *
     * @return the number of pages available
     */
    public final Integer getPagesAvailable() {
        return pagesAvailable;
    }

    /**
     *
     * @return the index of the next page
     */
    public final Integer getNextIndex() {
        return nextIndex;
    }

    /**
     *
     * @return the index of the previous page
     */
    public final Integer getPrevIndex() {
        return prevIndex;
    }

    /**
     *
     * @return the index of this page
     */
    public final Integer getCurrentIndex() {
        return currentIndex;
    }

    /**
     *
     * @return the total number of objects available.
     */
    public final Integer size() {
        return size;
    }

    /**
     *
     * @return the index of the first record in this result set
     */
    public final Integer getFirstRecord() {
        return firstRecord;
    }

    /**
     *
     * @return the index of the last record in this result set
     */
    public final Integer getLastRecord() {
        return lastRecord;
    }

    /**
     *
     * @return the records in this page
     */
    public final List<T> getRecords() {
        return records;
    }

    /**
     *
     * @return the page size
     */
    public final Integer getPageSize() {
        return pageSize;
    }

    @Override
    public final void addFacets(
            final String facetName, final List<Facet> newFacets) {
        this.facets.put(facetName, newFacets);
    }

    @Override
    public final List<Facet> getFacets(final String facetName) {
        return facets.get(facetName);
    }
}
