package org.emonocot.api;

import org.emonocot.model.common.SecuredObject;
import org.emonocot.model.user.Principal;
import org.emonocot.model.user.User;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public interface UserService extends Service<User>, UserDetailsManager,
        GroupManager {
    
    public <T extends SecuredObject> void addPermission(T object, Principal recipient, Permission permission, Class<T> clazz);
    public <T extends SecuredObject> void deletePermission(T object, Principal recipient, Permission permission, Class<T> clazz);
}
