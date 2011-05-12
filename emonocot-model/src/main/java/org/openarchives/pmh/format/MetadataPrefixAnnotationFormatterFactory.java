package org.openarchives.pmh.format;

import java.util.HashSet;
import java.util.Set;

import org.openarchives.pmh.MetadataPrefix;
import org.openarchives.pmh.format.annotation.MetadataPrefixFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 *
 * @author ben
 *
 */
public class MetadataPrefixAnnotationFormatterFactory implements
        AnnotationFormatterFactory<MetadataPrefixFormat> {

    /**
     *
     */
    private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

    static {
        FIELD_TYPES.add(MetadataPrefix.class);
    }

    @Override
    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public final Parser<?> getParser(final MetadataPrefixFormat metadataPrefix,
            final Class<?> fieldType) {
        return new MetadataPrefixFormatter();
    }

    @Override
    public final Printer<?> getPrinter(
            final MetadataPrefixFormat metadataPrefix,
            final Class<?> fieldType) {
       return new MetadataPrefixFormatter();
    }

}
