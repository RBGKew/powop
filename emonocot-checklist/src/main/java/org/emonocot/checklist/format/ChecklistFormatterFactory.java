package org.emonocot.checklist.format;

import org.openarchives.pmh.format.MetadataPrefixAnnotationFormatterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 *
 * @author ben
 *
 */
public class ChecklistFormatterFactory extends
        FormattingConversionServiceFactoryBean {

    @Override
    public final void installFormatters(final FormatterRegistry registry) {
        super.installFormatters(registry);
        registry.addFormatterForFieldAnnotation(
                new MetadataPrefixAnnotationFormatterFactory());
    }

}
