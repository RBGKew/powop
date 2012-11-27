package org.emonocot.pager;

import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public class DefaultPageImpl<T> extends AbstractPageImpl<T> {

    /**
     *
     */
    private static final long serialVersionUID = 7342101588074430414L;
    /**
     *
     */
    public static final Integer MAX_PAGE_LABELS = null;

    /**
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
    public DefaultPageImpl(final Integer count, final Integer newCurrentIndex,
            final Integer newPageSize, final List<T> newRecords, QueryResponse queryResponse) {
        super(count, newCurrentIndex, newPageSize, newRecords, queryResponse);
    }

    @Override
    public final String createLabel(
            final String startLabel, final String endLabel) {
        return startLabel + AbstractPageImpl.LABEL_DIVIDER + endLabel;
    }
}
