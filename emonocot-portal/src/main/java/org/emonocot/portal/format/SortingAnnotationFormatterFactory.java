package org.emonocot.portal.format;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.model.comms.Sorting;
import org.emonocot.portal.format.annotation.SortingFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 *
 * @author jk00kg
 *
 */
public class SortingAnnotationFormatterFactory implements
        AnnotationFormatterFactory<SortingFormat> {

   /**
    *
    */
   private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

   static {
       FIELD_TYPES.add(Sorting.class);
   }

    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    public final Parser<?> getParser(
            final SortingFormat sortingFormat,
            final Class<?> fieldType) {
        return new SortingFormatter();
    }

    public final Printer<?> getPrinter(
            final SortingFormat sortingFormat, final Class<?> fieldType) {
        return new SortingFormatter();
    }

}
