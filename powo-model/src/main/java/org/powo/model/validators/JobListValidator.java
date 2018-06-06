package org.powo.model.validators;

import org.powo.api.JobConfigurationService;
import org.powo.model.JobConfiguration;
import org.powo.model.JobList;
import org.powo.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class JobListValidator implements Validator {

	@Autowired
	Validator globalValidator;

	@Autowired
	JobConfigurationService jobConfigurationService;

	@Override
	public boolean supports(Class<?> clazz) {
		return JobList.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		JobList jobList = (JobList)target;

		for(JobConfiguration job : jobList.getJobConfigurations()) {
			try {
				jobConfigurationService.find(job.getIdentifier());
			} catch(NotFoundException e) {
				errors.reject("jobConfiguration[" + job.getIdentifier() + "]", "unknown");
			}
		}

		ValidationUtils.invokeValidator(globalValidator, jobList, errors);
	}

}
