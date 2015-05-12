/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
