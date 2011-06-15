package org.emonocot.harvest.common.index;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.springframework.batch.item.database.orm.HibernateQueryProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public class FullTextSessionHibernateItemReaderHelper<T> implements
        InitializingBean {

    /**
     *
     */
    private SessionFactory sessionFactory;

    /**
     *
     */
    private String queryString = "";

    /**
     *
     */
    private String queryName = "";

    /**
     *
     */
    private HibernateQueryProvider queryProvider;

    /**
     *
     */
    private FullTextSession session;

    /**
     * @param newQueryName
     *            name of a hibernate named query
     */
    public final void setQueryName(final String newQueryName) {
        this.queryName = newQueryName;
    }

    /**
     * @param newQueryString
     *            HQL query string
     */
    public final void setQueryString(final String newQueryString) {
        this.queryString = newQueryString;
    }

    /**
     * @param newQueryProvider
     *            Hibernate query provider
     */
    public final void setQueryProvider(
            final HibernateQueryProvider newQueryProvider) {
        this.queryProvider = newQueryProvider;
    }

    /**
     * @param newSessionFactory
     *            hibernate session factory
     */
    public final void setSessionFactory(
            final SessionFactory newSessionFactory) {
        this.sessionFactory = newSessionFactory;
    }

    /**
     * @throws Exception
     *             if there is a problem with the properties as set
     */
    public final void afterPropertiesSet() throws Exception {

        Assert.state(sessionFactory != null,
                "A SessionFactory must be provided");

        if (queryProvider == null) {
            Assert.notNull(sessionFactory, "session factory must be set");
            Assert.state(
                    StringUtils.hasText(queryString)
                            ^ StringUtils.hasText(queryName),
                    "queryString or queryName must be set");
        } else {
            // making sure that the appropriate (Hibernate) query provider is
            // set
            Assert.state(queryProvider != null,
                    "Hibernate query provider must be set");
        }

    }

    /**
     * Get a cursor over all of the results, with the forward-only flag set.
     *
     * @param fetchSize
     *            the fetch size to use retrieving the results
     * @param parameterValues
     *            the parameter values to use (or null if none).
     *
     * @return a forward-only {@link ScrollableResults}
     */
    public final ScrollableResults getForwardOnlyCursor(final int fetchSize,
            final Map<String, Object> parameterValues) {
        Query query = createQuery();
        if (parameterValues != null) {
            query.setProperties(parameterValues);
        }
        return query.setFetchSize(fetchSize).scroll(ScrollMode.FORWARD_ONLY);
    }

    /**
     * Open appropriate type of hibernate session and create the query.
     *
     * @return a hibernate query object
     */
    public final Query createQuery() {

        if (session == null) {
            Session newSession = sessionFactory.openSession();
            session = Search.createFullTextSession(newSession);
            session.setFlushMode(FlushMode.MANUAL);
            session.setCacheMode(CacheMode.IGNORE);
        }
        if (queryProvider != null) {
            queryProvider.setSession(session);
        } else {
            if (StringUtils.hasText(queryName)) {
                return session.getNamedQuery(queryName);
            } else {
                return session.createQuery(queryString);
            }
        }

        // If queryProvider is set use it to create a query
        return queryProvider.createQuery();

    }

    /**
     * Scroll through the results up to the item specified.
     *
     * @param cursor
     *            the results to scroll over
     * @param itemIndex
     *            the item index to jump to
     * @param flushInterval
     *            the interval to flush the session after
     */
    public final void jumpToItem(final ScrollableResults cursor,
            final int itemIndex, final int flushInterval) {
        for (int i = 0; i < itemIndex; i++) {
            cursor.next();
            if (i % flushInterval == 0) {
                session.flushToIndexes();
                session.clear(); // Clears in-memory cache
            }
        }
    }

    /**
     * Close the open session (stateful or otherwise).
     */
    public final void close() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    /**
     * Read a page of data, clearing the existing session (if necessary) first,
     * and creating a new session before executing the query.
     *
     * @param page
     *            the page to read (starting at 0)
     * @param pageSize
     *            the size of the page or maximum number of items to read
     * @param fetchSize
     *            the fetch size to use
     * @param parameterValues
     *            the parameter values to use (if any, otherwise null)
     * @return a collection of items
     */
    public final Collection<? extends T> readPage(final int page,
            final int pageSize, final int fetchSize,
            final Map<String, Object> parameterValues) {

        clear();

        Query query = createQuery();
        if (parameterValues != null) {
            query.setProperties(parameterValues);
        }
        @SuppressWarnings("unchecked")
        List<T> result = query.setFetchSize(fetchSize)
                .setFirstResult(page * pageSize).setMaxResults(pageSize).list();
        return result;

    }

    /**
     * Clear the session if stateful.
     */
    public final void clear() {
        if (session != null) {
            session.flushToIndexes();
            session.clear();
        }
    }

    /**
     *
     * @param t
     *            the object to index
     */
    public final void index(final T t) {
        session.index(t);
    }

    /**
     *
     */
    public final void optimizeIndexes() {
        SearchFactory searchFactory = session.getSearchFactory();
        searchFactory.optimize();
    }

}
