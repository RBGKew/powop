package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.common.Base;
import org.emonocot.persistence.dao.Dao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param sessionFactory Set the session factory
     */
    @Autowired
    public final void setHibernateSessionFactory(
            final SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

    @Override
    public final T load(final String identifier) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final T find(final String identifier) {
        // TODO Auto-generated method stub
        return null;
    }
}
