package org.emonocot.portal.format;

import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

public class PermissionFormatterRegistrar implements FormatterRegistrar {

	@Override
	public void registerFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldAnnotation(new PermissionAnnotationFormatterFactory());
	}

}
