package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.common.Base;
import org.emonocot.persistence.dao.Dao;
import org.hibernate.SessionFactory;
import org.hibernate.UnresolvableObjectException;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author ben
 *
 * @param <T>
 *            the type of object managed by this dao
 */
public abstract class DaoImpl<T extends Base> extends HibernateDaoSupport
        implements Dao<T> {
    /**
     *
     */
    private Class<T> type;

    /**
     *
     * @param newType Set the type of object handled by this DAO
     */
    public DaoImpl(final Class<T> newType) {
        this.type = newType;
    }

    /**
     *
     * @param sessionFactory Set the session factory
     */
    @Autowired
    public final void setHibernateSessionFactory(
            final SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

    @Override
    public final T load(final String identifier) {
        return load(identifier, null);
    }

    @Override
    public final T find(final String identifier) {
        return find(identifier, null);
    }

    @Override
    public T load(final String identifier, final String fetch) {
        if (fetch != null) {
          getSession().enableFetchProfile(fetch);
        }

        T t = (T) getSession().createCriteria(type)
                .add(Restrictions.eq("identifier", identifier)).uniqueResult();

        if (fetch != null) {
            getSession().disableFetchProfile(fetch);
        }

        if (t == null) {
            throw new HibernateObjectRetrievalFailureException(
                    new UnresolvableObjectException(identifier,
                            "Object could not be resolved"));
        }
        return t;
    }

    @Override
    public final T find(final String identifier, final String fetch) {
        if (fetch != null) {
            getSession().enableFetchProfile(fetch);
        }

        T t = (T) getSession().createCriteria(type)
        .add(Restrictions.eq("identifier", identifier)).uniqueResult();

        if (fetch != null) {
            getSession().disableFetchProfile(fetch);
        }
        return t;
    }
}
