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
    
    public void addPermission(SecuredObject object, Principal recipient, Permission permission, Class<? extends SecuredObject> clazz);
    public void deletePermission(SecuredObject object, Principal recipient, Permission permission, Class<? extends SecuredObject> clazz);
}
