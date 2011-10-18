package org.emonocot.api;

import org.emonocot.model.user.User;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 *
 * @author ben
 *
 */
public interface UserService extends Service<User>, UserDetailsManager,
        GroupManager {
}
