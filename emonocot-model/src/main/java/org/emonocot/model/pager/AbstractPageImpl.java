package org.emonocot.model.pager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.model.comms.Sorting;
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
    protected static final Integer MAX_PAGE_LABELS = 3;

    /**
     *
     */
    protected static final String LABEL_DIVIDER = " - ";

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(AbstractPageImpl.class);

    /**
     *
     */
    private Map<String, Object> parameters = new HashMap<String, Object>();

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
     *
     */
    private Map<Integer, String> pageNumbers;

    /**
     *
     */
    private ArrayList<Integer> indices;

    /**
     *
     */
    private Map<String, Integer> selectedFacets
        = new HashMap<String, Integer>();

    /**
     *
     */
    private Sorting sort;

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
        this.pageNumbers = new HashMap<Integer, String>();
        indices = new ArrayList<Integer>();
        if (count == 0) {
            pagesAvailable = 1;
        } else if (newPageSize != null) {
            if (0 == count % newPageSize) {
                pagesAvailable = count / newPageSize;

                Integer labelsStart = 0;
                if (this.currentIndex > AbstractPageImpl.MAX_PAGE_LABELS) {
                    labelsStart = this.currentIndex
                            - AbstractPageImpl.MAX_PAGE_LABELS;
                }

                Integer labelsEnd = pagesAvailable.intValue();
                if ((pagesAvailable - this.currentIndex)
                          > AbstractPageImpl.MAX_PAGE_LABELS) {
                    labelsEnd = this.currentIndex
                            + AbstractPageImpl.MAX_PAGE_LABELS;
                }

                for (int index = labelsStart; index < labelsEnd; index++) {
                    indices.add(index);
                    String startLabel = getLabel(index * pageSize);
                    String endLabel = getLabel(((index + 1) * pageSize) - 1);
                    pageNumbers.put(index, createLabel(startLabel, endLabel));
                }
            } else {
                pagesAvailable = (count / newPageSize) + 1;

                Integer labelsStart = 0;
                if (this.currentIndex > AbstractPageImpl.MAX_PAGE_LABELS) {
                    labelsStart = this.currentIndex
                            - AbstractPageImpl.MAX_PAGE_LABELS;
                }

                Integer labelsEnd = pagesAvailable.intValue();
                if ((pagesAvailable - this.currentIndex)
                        > AbstractPageImpl.MAX_PAGE_LABELS) {
                    labelsEnd = this.currentIndex
                            + AbstractPageImpl.MAX_PAGE_LABELS;
                    for (int index = labelsStart; index < labelsEnd; index++) {
                        indices.add(index);

                        String startLabel = getLabel(index * pageSize);
                        String endLabel =
                            getLabel(((index + 1) * pageSize) - 1);
                        pageNumbers.put(index,
                                createLabel(startLabel, endLabel));
                    }

                } else {
                    for (int index = labelsStart;
                         index < (labelsEnd - 1); index++) {
                        indices.add(index);
                        String startLabel = getLabel(index * pageSize);
                        String endLabel
                            = getLabel(((index + 1) * pageSize) - 1);
                        pageNumbers.put(index,
                                createLabel(startLabel, endLabel));
                    }
                    indices.add(pagesAvailable.intValue() - 1);
                    String startLabel = getLabel((pagesAvailable.intValue() - 1)
                            * pageSize);
                    String endLabel = getLabel(count.intValue() - 1);
                    pageNumbers.put(pagesAvailable.intValue() - 1,
                            createLabel(startLabel, endLabel));
                }

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
    public final Integer getSize() {
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

    public final void addFacets(
            final String facetName, final List<Facet> newFacets) {
        this.facets.put(facetName, newFacets);
    }

    public final Map<String, List<Facet>> getFacets() {
        return facets;
    }

    public final List<String> getFacetNames() {
        List<String> facetNames = new ArrayList<String>();
        facetNames.addAll(facets.keySet());
        Collections.sort(facetNames, new FacetNameComparator());
        return facetNames;
    }

    public final void putParam(final String name, final Object value) {
        this.parameters.put(name, value);
    }

    public final Map<String, Object> getParams() {
        return parameters;
    }

    public final String getPageNumber(final int index) {
        return pageNumbers.get(index);
    }

    /**
     *
     * @param i set the index
     * @return the label
     */
    protected final String getLabel(final Integer i) {
        Integer label = new Integer(i + 1);
        return label.toString();
    }

    /**
     *
     * @param startLabel Set the start label
     * @param endLabel Set the end label
     * @return a page label
     */
    protected abstract String createLabel(String startLabel, String endLabel);

    /**
     *
     * @return the list of indices for which we have a page label
     */
    public final List<Integer> getIndices() {
        return indices;
    }

    /**
     *
     * @return a list of the names of selected facets
     */
    public final Set<String> getSelectedFacetNames() {
        return selectedFacets.keySet();
    }

    /**
     *
     * @return The index of the selected facet, or null if the facet is not
     *         selected
     */
    public final Map<String, Integer> getSelectedFacets() {
        return selectedFacets;
    }

    /**
     *
     * @param facetName
     *            Set the facet name
     * @return true if the facet is selected, false otherwise
     */
    public final boolean isFacetSelected(final String facetName) {
        return selectedFacets.containsKey(facetName);
    }

    /**
     * @param facetName
     *            Set the facet name
     * @param selected
     *            Set the index of the selected facet
     */
    public final void setSelectedFacet(final String facetName,
            final Integer selected) {
        selectedFacets.put(facetName, selected);
    }

    /**
     * @return the sort
     */
    public final Sorting getSort() {
        return sort;
    }

    /**
     * @param newSort set the sorting
     */
    public final void setSort(final Sorting newSort) {
        this.sort = newSort;
    }
}
