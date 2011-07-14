package org.emonocot.portal.format;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.portal.controller.FacetRequest;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 *
 * @author ben
 *
 */
public class FacetRequestAnnotationFormatterFactory implements
        AnnotationFormatterFactory<FacetRequestFormat> {

   /**
    *
    */
   private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

   static {
       FIELD_TYPES.add(FacetRequest.class);
   }

    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    public final Parser<?> getParser(
            final FacetRequestFormat facetRequestFormat,
            final Class<?> fieldType) {
        return new FacetRequestFormatter();
    }

    public final Printer<?> getPrinter(
            final FacetRequestFormat arg0, final Class<?> fieldType) {
        return new FacetRequestFormatter();
    }

}
