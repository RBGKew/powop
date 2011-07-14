package org.emonocot.harvest.common.index;

import java.util.List;
import java.util.Map;

import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.orm.HibernateQueryProvider;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 *
 * @author ben
 *
 * @param <T>
 *            the type of object this writer writes
 */
public class HibernateSearchIndexingWriter<T> extends
        AbstractItemCountingItemStreamItemReader<T> implements ItemStream,
        InitializingBean, ItemWriter<T> {

    /**
     *
     */
    private static final int CLEAR_SESSION_SIZE = 100;

    /**
     *
     */
    private FullTextSessionHibernateItemReaderHelper<T> helper
        = new FullTextSessionHibernateItemReaderHelper<T>();

    /**
     *
     */
    public HibernateSearchIndexingWriter() {
        setName(ClassUtils.getShortName(HibernateSearchIndexingWriter.class));
    }

    /**
     *
     */
    private ScrollableResults cursor;

    /**
     *
     */
    private boolean initialized = false;

    /**
     *
     */
    private int fetchSize;

    /**
     *
     */
    private Map<String, Object> parameterValues;

    /**
     * @throws Exception
     *             if there is a problem
     */
    public final void afterPropertiesSet() throws Exception {
        Assert.state(fetchSize >= 0, "fetchSize must not be negative");
        helper.afterPropertiesSet();
    }

    /**
     * The parameter values to apply to a query (map of name:value).
     *
     * @param newParameterValues
     *            the parameter values to set
     */
    public final void setParameterValues(
            final Map<String, Object> newParameterValues) {
        this.parameterValues = newParameterValues;
    }

    /**
     * A query name for an externalized query. Either this or the {
     * {@link #setQueryString(String) query string} or the {
     * {@link #setQueryProvider(HibernateQueryProvider) query provider} should
     * be set.
     *
     * @param queryName
     *            name of a hibernate named query
     */
    public final void setQueryName(final String queryName) {
        helper.setQueryName(queryName);
    }

    /**
     * Fetch size used internally by Hibernate to limit amount of data fetched
     * from database per round trip.
     *
     * @param newFetchSize
     *            the fetch size to pass down to Hibernate
     */
    public final void setFetchSize(final int newFetchSize) {
        this.fetchSize = newFetchSize;
    }

    /**
     * A query provider. Either this or the {{@link #setQueryString(String)
     * query string} or the {{@link #setQueryName(String) query name} should be
     * set.
     *
     * @param queryProvider
     *            Hibernate query provider
     */
    public final void setQueryProvider(
            final HibernateQueryProvider queryProvider) {
        helper.setQueryProvider(queryProvider);
    }

    /**
     * A query string in HQL. Either this or the {
     * {@link #setQueryProvider(HibernateQueryProvider) query provider} or the {
     * {@link #setQueryName(String) query name} should be set.
     *
     * @param queryString
     *            HQL query string
     */
    public final void setQueryString(final String queryString) {
        helper.setQueryString(queryString);
    }

    /**
     * The Hibernate SessionFactory to use the create a session.
     *
     * @param sessionFactory
     *            the {@link SessionFactory} to set
     */
    public final void setSessionFactory(final SessionFactory sessionFactory) {
        helper.setSessionFactory(sessionFactory);
    }

    /**
     * @return a single object of type T or null if there are no more objects
     * @throws Exception
     *             if there is a problem reading an object from the cursor
     */
    protected final T doRead() throws Exception {
        if (cursor.next()) {
            Object[] data = cursor.get();

            if (data.length > 1) {
                // If there are multiple items this must be a projection
                // and T is an array type.
                @SuppressWarnings("unchecked")
                T item = (T) data;
                return item;
            } else {
                // Assume if there is only one item that it is the data the user
                // wants.
                // If there is only one item this is going to be a nasty shock
                // if T is an array type but there's not much else we can do...
                @SuppressWarnings("unchecked")
                T item = (T) data[0];
                return item;
            }

        }
        return null;
    }

    /**
     * Open hibernate session and create a forward-only cursor for the query.
     *
     * @throws Exception
     *             if there is a problem opening getting the cursor
     */
    protected final void doOpen() throws Exception {
        Assert.state(!initialized,
                "Cannot open an already opened ItemReader, call close first");
        cursor = helper.getForwardOnlyCursor(fetchSize, parameterValues);
        initialized = true;
    }

    /**
     * Update the context and clear the session if stateful.
     *
     * @param executionContext
     *            the current {@link ExecutionContext}
     */
    @Override
    public final void update(final ExecutionContext executionContext) {
        super.update(executionContext);
        helper.clear();
    }

    /**
     * Wind forward through the result set to the item requested. Also clears
     * the session every now and then (if stateful) to avoid memory problems.
     * The frequency of session clearing is the larger of the fetch size (if
     * set) and 100.
     *
     * @param itemIndex
     *            the first item to read
     * @throws Exception
     *             if there is a problem
     * @see AbstractItemCountingItemStreamItemReader#jumpToItem(int)
     */
    @Override
    protected final void jumpToItem(final int itemIndex) throws Exception {
        int flushSize = Math.max(fetchSize,
                HibernateSearchIndexingWriter.CLEAR_SESSION_SIZE);
        helper.jumpToItem(cursor, itemIndex, flushSize);
    }

    /**
     * Close the cursor and hibernate session.
     *
     * @throws Exception
     *             if there is a problem closing the session
     */
    protected final void doClose() throws Exception {

        initialized = false;

        if (cursor != null) {
            cursor.close();
        }

        helper.optimizeIndexes();
        helper.close();

    }

    public final void write(final List<? extends T> list) throws Exception {
        for (T t : list) {
            helper.index(t);
        }
    }
}
