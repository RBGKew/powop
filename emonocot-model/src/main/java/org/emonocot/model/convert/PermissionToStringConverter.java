package org.emonocot.model.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class PermissionToStringConverter implements
        Converter<Permission, String> {

    /**
     * @param permission the permission to convert
     * @return a string
     */
    public final String convert(final Permission permission) {
        if (permission == null) {
            return null;
        } else if (permission.equals(BasePermission.CREATE)) {
            return "CREATE";
        } else if (permission.equals(BasePermission.READ)) {
            return "READ";
        } else if (permission.equals(BasePermission.WRITE)) {
            return "WRITE";
        } else if (permission.equals(BasePermission.DELETE)) {
            return "DELETE";
        } else if (permission.equals(BasePermission.ADMINISTRATION)) {
            return "ADMINISTRATION";
        } else {
            throw new IllegalArgumentException(permission
                    + " cannot be converted into a string");
        }
    }

}
