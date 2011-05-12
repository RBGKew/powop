package org.openarchives.pmh.format;

import java.util.HashSet;
import java.util.Set;

import org.openarchives.pmh.SetSpec;
import org.openarchives.pmh.format.annotation.SetSpecFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 *
 * @author ben
 *
 */
public class SetSpecAnnotationFormatterFactory implements
        AnnotationFormatterFactory<SetSpecFormat> {

    /**
     *
     */
    private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

    static {
        FIELD_TYPES.add(SetSpec.class);
    }

    @Override
    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public final Parser<?> getParser(final SetSpecFormat setSpec,
            final Class<?> fieldType) {
        return new SetSpecFormatter();
    }

    @Override
    public final Printer<?> getPrinter(
            final SetSpecFormat setSpec,
            final Class<?> fieldType) {
       return new SetSpecFormatter();
    }

}
