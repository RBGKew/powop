package org.emonocot.persistence.dao.hibernate;

import org.apache.commons.beanutils.PropertyUtils;
import org.emonocot.model.common.Base;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.Dao;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.UnresolvableObjectException;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
     * @param profile Set the name of the profile
     * @return a list of Fetch instances
     */
    protected abstract Fetch[] getProfile(String profile);

    /**
     *
     * @param criteria Set a Criteria instance
     * @param fetch Set the name of the fetch profile
     */
    protected void enableProfilePreQuery(final Criteria criteria,
            final String fetch) {
        if (fetch != null) {
            for (Fetch f : getProfile(fetch)) {
                if (f.getMode().equals(FetchMode.JOIN)) {
                    criteria.setFetchMode(f.getAssociation(), f.getMode());
                }
            }
        }
    }

    /**
    *
    * @param t Set a the fetched object
    * @param fetch Set the name of the fetch profile
    */
   protected final void enableProfilePostQuery(final T t,
           final String fetch) {
       if (fetch != null && t != null) {
           for (Fetch f : getProfile(fetch)) {
               if (f.getMode().equals(FetchMode.SELECT)) {
                   Object proxy;
                    try {
                        proxy = PropertyUtils.getProperty(t, f.getAssociation());
                    } catch (Exception e) {
                        throw new InvalidDataAccessApiUsageException(
                                "Cannot get proxy " + f.getAssociation()
                                        + " for class " + type, e);
                    }
                   Hibernate.initialize(proxy);
               }
           }
       }
   }

    /**
     *
     */
    protected Class<T> type;

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

    /**
     * @param identifier Set the identifier
     */
    public final void delete(final String identifier) {
        T t = load(identifier);
        getSession().delete(t);
    }

    /**
     * @param identifier set the identifier
     * @return the loaded object
     */
    public final T load(final String identifier) {
        return load(identifier, null);
    }

    /**
     * @param identifier Set the identifier
     * @return the object, or null if the object cannot be found
     */
    public final T find(final String identifier) {
        return find(identifier, null);
    }

    /**
     * @param identifier Set the identifier
     * @param fetch Set the fetch profile (can be null)
     * @return the loaded object
     */
    public T load(final String identifier, final String fetch) {

        Criteria criteria = getSession().createCriteria(type).add(
                Restrictions.eq("identifier", identifier));

        enableProfilePreQuery(criteria, fetch);

        T t = (T) criteria.uniqueResult();

        if (t == null) {
            throw new HibernateObjectRetrievalFailureException(
                    new UnresolvableObjectException(identifier,
                            "Object could not be resolved"));
        }
        enableProfilePostQuery(t, fetch);
        return t;
    }

    /**
     * @param identifier Set the identifer
     * @param fetch Set the fetch profile
     * @return the object or null if it cannot be found
     */
    public T find(final String identifier, final String fetch) {
        Criteria criteria = getSession().createCriteria(type)
        .add(Restrictions.eq("identifier", identifier));
        enableProfilePreQuery(criteria, fetch);
        T t = (T) criteria.uniqueResult();
        enableProfilePostQuery(t, fetch);

        return t;
    }

   /**
    *
    * @param t The object to save.
    * @return the saved object
    */
   public final T save(final T t) {
       getSession().save(t);
       return t;
   }

  /**
   *
   * @param t The object to save.
   */
  public final void saveOrUpdate(final T t) {
      getSession().saveOrUpdate(t);
  }

  /**
  *
  * @param t The object to update.
  */
 public final void update(final T t) {
     getSession().update(t);
 }
}
