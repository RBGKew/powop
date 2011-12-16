package org.emonocot.api;

import java.util.List;

import org.emonocot.model.common.SecuredObject;
import org.emonocot.model.user.User;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 *
 * @author ben
 *
 */
public interface UserService extends Service<User>, UserDetailsManager,
        GroupManager {

    /**
     *
     * @param object the object to add the permission to
     * @param recipient the recipient recieving the permission
     * @param permission the permission itself
     * @param clazz the class of object
     */
    void addPermission(SecuredObject object, String recipient,
            Permission permission, Class<? extends SecuredObject> clazz);
   /**
    *
    * @param object the object to remove the permission from
    * @param recipient the recipient with the permission
    * @param permission the permission itself
    * @param clazz the class of object
    */
    void deletePermission(SecuredObject object, String recipient,
            Permission permission, Class<? extends SecuredObject> clazz);

    /**
     *
     * @param recipient the recipent of the permissions
     * @return a list of Access Control Entries
     */
    List<Object[]> listAces(String recipient);
}
