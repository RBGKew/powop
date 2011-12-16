package org.emonocot.portal.format;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class PermissionFormatter
    implements Formatter<Permission> {

    /**
     * @param permission Set the permission
     * @param locale Set the locale
     * @return the facet request as a string
     */
    public final String print(
            final Permission permission, final Locale locale) {
        if (permission.equals(BasePermission.READ)) {
            return "READ";
        } else if (permission.equals(BasePermission.WRITE)) {
            return "WRITE";
        } else if (permission.equals(BasePermission.CREATE)) {
            return "CREATE";
        } else if (permission.equals(BasePermission.DELETE)) {
            return "DELETE";
        } else if (permission.equals(BasePermission.ADMINISTRATION)) {
            return "ADMINISTRATION";
        } else {
            return "UNKNOWN_PERMISSON";
        }
    }

    /**
     * @param permission the facet request as a string
     * @param locale Set the locale
     * @return a Permission object
     * @throws ParseException if there is a problem parsing the string
     */
    public final Permission parse(
            final String permission, final Locale locale)
            throws ParseException {
        if (permission.equals("READ")) {
            return BasePermission.READ;
        } else if (permission.equals("WRITE")) {
            return BasePermission.WRITE;
        } else if (permission.equals("CREATE")) {
            return BasePermission.CREATE;
        } else if (permission.equals("DELETE")) {
            return BasePermission.DELETE;
        } else if (permission.equals("ADMINISTRATION")) {
            return BasePermission.ADMINISTRATION;
        } else {
            throw new ParseException(permission
                    + " is not an acceptable value for a Permission", 0);
        }
    }

}
