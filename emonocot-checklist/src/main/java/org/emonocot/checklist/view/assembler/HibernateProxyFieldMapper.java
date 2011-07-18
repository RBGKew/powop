package org.emonocot.checklist.view.assembler;

import org.dozer.CustomFieldMapper;
import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.FieldMap;
import org.hibernate.collection.PersistentSet;

/**
 * FieldMapper which can handle un-initialized collections, returning NULL if
 * the collection is uninitialized.
 *
 * @author ben
 *
 */
public class HibernateProxyFieldMapper implements CustomFieldMapper {

    /**
     * @param source the source object
     * @param destination the destination object
     * @param sourceFieldValue the value of the source field
     * @param classMap the classmap
     * @param fieldMapping the field mapping
     * @return true if dozer should map or false if not
     */
    public final boolean mapField(final Object source, Object destination,
            final Object sourceFieldValue, final ClassMap classMap,
            final FieldMap fieldMapping) {
        // Check if field is a Hibernate PersistentSet
        if (!(sourceFieldValue instanceof PersistentSet)) {
            // Allow dozer to map as normal
            return false;
        }

        // Check if field is already initialized
        if (((PersistentSet) sourceFieldValue).wasInitialized()) {
            // Allow dozer to map as normal
            return false;
        }

        // Set destination to null, and tell dozer that the field is mapped
        destination = null;
        return true;
    }
}
