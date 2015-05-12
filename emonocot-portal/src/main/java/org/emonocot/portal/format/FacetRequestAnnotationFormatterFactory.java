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
