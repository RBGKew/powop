package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.UserDao;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class UserDaoImpl extends DaoImpl<User> implements UserDao {

    /**
     *
     */
   public UserDaoImpl() {
        super(User.class);
    }

   /**
    *
    */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
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
   *
   * @param identifier
   *            Set the identifier of the user you would like to retrieve
   * @param fetch Set the fetch profile to use
   * @return the user or throw and exception if that user does not exist
   */
  @Override
  public final User load(String identifier, String fetch) {
      User user = super.load(identifier, fetch);
      initializeUserPermissions(user);
      return user;
  }

    /**
     *
     * @param user
     *            Set the user to initialize
     */
    private void initializeUserPermissions(final User user) {
        if (user != null) {
            Hibernate.initialize(user.getPermissions());
            Hibernate.initialize(user.getGroups());
            for (Group group : user.getGroups()) {
                Hibernate.initialize(group.getPermissions());
            }
        }
    }

    /**
     *
     * @param identifier
     *            Set the identifier of the user you would like to retrieve
     * @param fetch
     *            Set the fetch profile to use
     * @return the user or null if that user does not exist
     */
    @Override
    public final User find(String identifier, String fetch) {
        User user = super.find(identifier, fetch);
        initializeUserPermissions(user);
        return user;
    }
}
