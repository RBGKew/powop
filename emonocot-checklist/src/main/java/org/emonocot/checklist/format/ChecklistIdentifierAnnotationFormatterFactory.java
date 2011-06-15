package org.emonocot.checklist.format;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.checklist.format.annotation.ChecklistIdentifierFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 *
 * @author ben
 *
 */
public class ChecklistIdentifierAnnotationFormatterFactory implements
        AnnotationFormatterFactory<ChecklistIdentifierFormat> {

    /**
    *
    */
   private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

   static {
       FIELD_TYPES.add(Long.class);
   }

   @Override
   public final Set<Class<?>> getFieldTypes() {
       return FIELD_TYPES;
   }

    @Override
    public final Parser<?> getParser(
            final ChecklistIdentifierFormat checklistIdentifierFormat,
            final Class<?> fieldType) {
        return new ChecklistIdentifierFormatter();
    }

    @Override
    public final Printer<?> getPrinter(
            final ChecklistIdentifierFormat checklistIdentifierFormat,
            final Class<?> fieldType) {
        return new ChecklistIdentifierFormatter();
    }

}
