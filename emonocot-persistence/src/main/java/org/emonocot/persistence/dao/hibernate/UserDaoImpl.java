package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.User;
import org.emonocot.persistence.dao.UserDao;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

   /**
    *
    */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
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
     * @param user Set the user
     */
    public final void update(final User user) {
        getSession().update(user);
    }

    /**
     * @param username Set the username
     * @param fetch Set the name of the fetch profile
     * @return a user or null if the user does not exist
     */
    public final User find(final String username, final String fetch) {
        Criteria criteria = getSession().createCriteria(User.class).add(
                Restrictions.eq("identifier", username));
        enableProfilePreQuery(criteria, fetch);
        User result = (User) criteria.uniqueResult();

        if (result == null) {
            return null;
        } else {
            Hibernate.initialize(result.getPermissions());
            Hibernate.initialize(result.getGroups());
            for (Group group : result.getGroups()) {
                Hibernate.initialize(group.getPermissions());
            }
        }
        enableProfilePostQuery(result, fetch);
        return result;
    }

    /**
     * @param user the user to save
     */
    public final void save(final User user) {
        getSession().save(user);
    }

    /**
     * @param user the user to delete
     */
    public final void delete(final User user) {
        getSession().delete(user);
    }

    /**
     * @param username Set the username
     * @param fetch Set the name of the fetch profile
     * @return a user
     */
    public final User load(final String username, final String fetch) {
        Criteria criteria = getSession().createCriteria(User.class)
        .add(Restrictions.eq("identifier", username));
        enableProfilePreQuery(criteria, fetch);
        User result = (User) criteria.uniqueResult();
        if (result == null) {
            throw new ObjectRetrievalFailureException(username, User.class);
        }
        Hibernate.initialize(result.getPermissions());
        Hibernate.initialize(result.getGroups());
        for (Group group : result.getGroups()) {
            Hibernate.initialize(group.getPermissions());
        }
        enableProfilePostQuery(result, fetch);

        return result;
    }

    /**
    *
    * @param criteria Set a Criteria instance
    * @param fetch Set the name of the fetch profile
    */
   protected final void enableProfilePreQuery(final Criteria criteria,
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
   * @param user Set a the fetched object
   * @param fetch Set the name of the fetch profile
   */
  protected final void enableProfilePostQuery(final User user,
          final String fetch) {
      if (fetch != null && user != null) {
          for (Fetch f : getProfile(fetch)) {
              if (f.getMode().equals(FetchMode.SELECT)) {
                  Object proxy;
                   try {
                       proxy = PropertyUtils.getProperty(user, f.getAssociation());
                   } catch (Exception e) {
                       throw new InvalidDataAccessApiUsageException(
                               "Cannot get proxy " + f.getAssociation()
                                       + " for class " + User.class, e);
                   }
                  Hibernate.initialize(proxy);
              }
          }
      }
  }


  /**
   *
   * @param profile Set the name of the fetch profile
   * @return a list of associated objects to fetch
   */
  protected final Fetch[] getProfile(final String profile) {
      return UserDaoImpl.FETCH_PROFILES.get(profile);
  }

  /**
   * @param username Set the username
   * @return a user
   */
  public final User load(final String username) {
        return load(username, null);
  }

  /**
   * @param username Set the username
   * @return the user or null if the user does not exist
   */
  public final User find(final String username) {
    return find(username, null);
  }

}
