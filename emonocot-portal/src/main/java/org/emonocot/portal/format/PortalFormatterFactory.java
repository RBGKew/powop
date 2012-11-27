package org.emonocot.portal.format;

import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 *
 * @author ben
 *
 */
public class PortalFormatterFactory extends
        FormattingConversionServiceFactoryBean {

    @Override
    public final void installFormatters(final FormatterRegistry registry) {
        super.installFormatters(registry);

        registry.addFormatterForFieldAnnotation(new FacetRequestAnnotationFormatterFactory());
        registry.addFormatterForFieldAnnotation(new PermissionAnnotationFormatterFactory());
    }

}
