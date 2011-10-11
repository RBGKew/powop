package org.emonocot.api;

import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 *
 * @author ben
 *
 */
public interface UserService extends UserDetailsManager, GroupManager {

}
