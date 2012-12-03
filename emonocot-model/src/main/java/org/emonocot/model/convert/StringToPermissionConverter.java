package org.emonocot.model.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class StringToPermissionConverter implements
        Converter<String, Permission> {

    /**
     * @param permission Set the string value
     * @return a permission
     */
    public final Permission convert(final String permission) {
        if (permission == null) {
            return null;
        } else if (permission.equals("CREATE")) {
            return BasePermission.CREATE;
        } else if (permission.equals("READ")) {
            return BasePermission.READ;
        } else if (permission.equals("WRITE")) {
            return BasePermission.WRITE;
        } else if (permission.equals("DELETE")) {
            return BasePermission.DELETE;
        } else if (permission.equals("ADMINISTRATION")) {
            return BasePermission.ADMINISTRATION;
        } else {
            throw new IllegalArgumentException(permission
                    + " is not a valid value for a Permission");
        }
    }

}
