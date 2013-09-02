package org.emonocot.portal.controller.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.emonocot.model.marshall.json.PermissionDeserializer;
import org.emonocot.model.marshall.json.PermissionSerializer;
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
    private String object;

    /**
     *
     */
    private Permission permission;

    /**
     *
     */
    private String principal;

    /**
     *
     */
    private Class clazz;

    /**
     * @return the clazz
     */
    public final Class getClazz() {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public final void setClazz(final Class clazz) {
        this.clazz = clazz;
    }

    /**
     *
     * @return the secured object
     */
    public final String getObject() {
        return object;
    }

    /**
     *
     * @return the permission
     */
    @JsonSerialize(using = PermissionSerializer.class)
    public final Permission getPermission() {
        return permission;
    }

    /**
     *
     * @param object Set the secured object
     */
    public final void setObject(final String object) {
        this.object = object;
    }

    /**
     *
     * @param permission Set the permission
     */
     @JsonDeserialize(using = PermissionDeserializer.class)
    public final void setPermission(final Permission permission) {
        this.permission = permission;
    }

    /**
     *
     * @return the principal
     */
    public final String getPrincipal() {
        return principal;
    }

    /**
     *
     * @param principal Set the principal
     */
    public final void setPrincipal(String principal) {
        this.principal = principal;
    }

}
