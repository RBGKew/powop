package org.emonocot.portal.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
	
	@Override
	public void initialize(FieldMatch constraintAnnotation) {
		firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
            final Object firstObj = PropertyUtils.getProperty(value, firstFieldName);
            final Object secondObj = PropertyUtils.getProperty(value, secondFieldName);

            return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ignore) {
            // ignore
        }
        return true;
	}

}
