package org.emonocot.portal.format;

import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

public class FacetRequestFormatterRegistrar implements FormatterRegistrar {

	@Override
	public void registerFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldAnnotation(new FacetRequestAnnotationFormatterFactory());
	}

}
