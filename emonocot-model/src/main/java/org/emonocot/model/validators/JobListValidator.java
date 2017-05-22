package org.emonocot.model.validators;

import org.emonocot.api.JobConfigurationService;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.JobList;
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
			if(jobConfigurationService.get(job.getId()) == null) {
				errors.reject("jobConfiguration[" + job.getId() + "]", "unknown");
			}
		}

		ValidationUtils.invokeValidator(globalValidator, jobList, errors);
	}

}
