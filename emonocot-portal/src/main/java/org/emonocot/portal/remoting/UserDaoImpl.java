package org.emonocot.portal.remoting;

import org.emonocot.model.auth.User;
import org.emonocot.persistence.dao.UserDao;
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
        super(User.class, "user");
    }
}
