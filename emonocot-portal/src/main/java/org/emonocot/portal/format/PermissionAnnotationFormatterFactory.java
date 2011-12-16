package org.emonocot.portal.format;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.portal.format.annotation.PermissionFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class PermissionAnnotationFormatterFactory implements
        AnnotationFormatterFactory<PermissionFormat> {

    /**
    *
    */
    private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

    static {
        FIELD_TYPES.add(Permission.class);
    }

   /**
    * @return the field types supported by this annotation formatter factory
    */
    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    /**
     * @param permissionFormat the annotation
     * @param fieldType the field type
     * @return a parser
     */
    public final Parser<?> getParser(final PermissionFormat permissionFormat,
            final Class<?> fieldType) {
        return new PermissionFormatter();
    }

    /**
     * @param permissionFormat the annotation
     * @param fieldType the field type
     * @return a printer
     */
    public final Printer<?> getPrinter(final PermissionFormat permissionFormat,
            final Class<?> fieldType) {
        return new PermissionFormatter();
    }

}
