package org.emonocot.portal.model;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.common.SecuredObject;
import org.emonocot.model.marshall.json.AnnotatableObjectDeserializer;
import org.emonocot.model.marshall.json.AnnotatableObjectSerializer;
import org.emonocot.model.marshall.json.PermissionSerializer;
import org.emonocot.model.marshall.json.PermissionDeserializer;
import org.emonocot.model.marshall.json.PrincipalSerializer;
import org.emonocot.model.marshall.json.PrincipalDeserializer;
import org.emonocot.model.user.Principal;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class AceDto {
    /**
     *
     */
    private SecuredObject object;
    
    /**
     *
     */
    private Permission permission;
    
    /**
     *
     */
    private Principal principal;

    /**
     *
     * @return the secured object
     */
    @JsonSerialize(using = AnnotatableObjectSerializer.class)
    public SecuredObject getObject() {
        return object;
    }
    
    /**
     *
     * @return the permission
     */
    @JsonSerialize(using = PermissionSerializer.class)
    public Permission getPermission() {
        return permission;
    }

    /**
     *
     * @param object Set the secured object
     */
    @JsonDeserialize(using = AnnotatableObjectDeserializer.class)
    public void setObject(SecuredObject object) {
        this.object = object;
    }

    /**
     *
     * @param permission Set the permission
     */
     @JsonDeserialize(using = PermissionDeserializer.class)
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     *
     * @return the principal
     */
    @JsonSerialize(using = PrincipalSerializer.class)
    public Principal getPrincipal() {
        return principal;
    }

    /**
     *
     * @param principal Set the principal
     */
    @JsonDeserialize(using = PrincipalDeserializer.class)
    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

}
