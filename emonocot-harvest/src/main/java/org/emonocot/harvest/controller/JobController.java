package org.emonocot.harvest.controller;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.JobList;
import org.emonocot.model.constants.JobType;
import org.emonocot.model.constants.SchedulingPeriod;
import org.emonocot.model.exception.InvalidEntityException;
import org.emonocot.model.marshall.json.JobSchedule;
import org.emonocot.model.validators.JobListValidator;
import org.emonocot.pager.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

import org.emonocot.api.JobConfigurationService;
import org.emonocot.api.JobListService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/1/job", produces = "application/json")
public class JobController {

	Logger logger = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private JobListService jobListService;

	@Autowired
	private JobConfigurationService jobConfigurationService;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobListValidator validator;

	@GetMapping(path = "/types")
	public ResponseEntity<JobType[]> jobTypes() {
		return new ResponseEntity<>(JobType.values(), HttpStatus.OK);
	}

	@GetMapping(path = "/schedulingPeriods")
	public ResponseEntity<SchedulingPeriod[]> schedulingPeriods() {
		return new ResponseEntity<>(SchedulingPeriod.values(), HttpStatus.OK);
	}

	@GetMapping(path = "/configuration")
	public ResponseEntity<Page<JobConfiguration>> listJobConfigurations(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "perPage", required = false, defaultValue = "30") Integer perPage) {
		return new ResponseEntity<>(jobConfigurationService.list(page, perPage), HttpStatus.OK);
	}

	@GetMapping(path = "/configuration/{id}")
	public ResponseEntity<JobConfiguration> getJobConfiguration(@PathVariable Long id) {
		return new ResponseEntity<>(jobConfigurationService.get(id), HttpStatus.OK);
	}

	@PostMapping(path = "/configuration/{id}/run")
	public ResponseEntity<JobConfiguration> runJobConfiguration(@PathVariable Long id) throws JobExecutionException {
		JobConfiguration jobConfiguration = jobConfigurationService.get(id);
		jobLauncher.launch(new JobLaunchRequest(jobConfiguration));
		logger.info("Running {}", jobConfiguration);
		jobConfiguration.setJobStatus(BatchStatus.STARTING);

		return new ResponseEntity<>(jobConfiguration, HttpStatus.OK);
	}

	@GetMapping(path = "/list")
	public ResponseEntity<Page<JobList>> listJobLists(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "perPage", required = false, defaultValue = "30") Integer perPage) {
		return new ResponseEntity<>(jobListService.list(page, perPage), HttpStatus.OK);
	}

	@PostMapping(path = "/list")
	public ResponseEntity<JobList> createJobList(
			@Valid @RequestBody JobList jobList,
			BindingResult result) {

		ValidationUtils.invokeValidator(validator, jobList, result);
		logger.debug("Creating {}", jobList);

		if (result.hasErrors()) {
			throw new InvalidEntityException(JobList.class, result);
		}

		jobListService.save(jobList);

		return new ResponseEntity<>(jobList, HttpStatus.CREATED);
	}

	@PostMapping(path = "/list/{id}/schedule")
	public ResponseEntity<JobList> scheduleJobList(
			@PathVariable Long id,
			@RequestBody JobSchedule schedule) {

		return new ResponseEntity<>(jobListService.schedule(id, schedule), HttpStatus.OK);
	}

	@PostMapping(path = "/list/{id}")
	public ResponseEntity<JobList> updateJobList(
			@PathVariable Long id,
			@Valid @RequestBody JobList jobList,
			BindingResult result) {

		ValidationUtils.invokeValidator(validator, jobList, result);

		if (result.hasErrors()) {
			throw new InvalidEntityException(JobList.class, result);
		}

		logger.debug("Updating {}", jobList);
		jobListService.save(jobList);

		return new ResponseEntity<>(jobList, HttpStatus.OK);
	}

	@GetMapping(path = "/list/{id}")
	public ResponseEntity<JobList> getJobList(@PathVariable Long id) {
		return new ResponseEntity<>(jobListService.get(id), HttpStatus.OK);
	}

}