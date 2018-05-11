package org.powo.model.validators;

import org.powo.model.marshall.json.ResourceWithJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ResourceWithJobValidator implements Validator {

	@Autowired
	@Qualifier("globalValidator")
	Validator globalValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return ResourceWithJob.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ResourceWithJob rwj = (ResourceWithJob) target;
		ValidationUtils.invokeValidator(globalValidator, rwj, errors);
		errors.pushNestedPath("resource");
		ValidationUtils.invokeValidator(globalValidator, rwj.getResource(), errors);
		// if the organisation does not have an ID, it could not be found during deserialization
		if(rwj.getResource() != null
				&& rwj.getResource().getOrganisation() != null
				&& rwj.getResource().getOrganisation().getId() == null) {
			errors.rejectValue("organisation", "not.found", "Organisation[" + rwj.getResource().getOrganisation().getIdentifier() + "] not found");
		}
		errors.popNestedPath();
	}

}
