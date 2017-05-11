package org.emonocot.harvest.controller;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.harvest.service.JobListService;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.JobList;
import org.emonocot.model.constants.JobType;
import org.emonocot.pager.Page;
import org.emonocot.service.impl.JobConfigurationService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/1/job", produces = "application/json")
public class JobController {

	@Autowired
	private JobListService jobListService;

	@Autowired
	private JobConfigurationService jobConfigurationService;

	@Autowired
	private JobLauncher jobLauncher;

	@GetMapping(path = "/types")
	public ResponseEntity<JobType[]> jobTypes() {
		return new ResponseEntity<>(JobType.values(), HttpStatus.OK);
	}

	@GetMapping(path = "/configurations")
	public ResponseEntity<Page<JobConfiguration>> listJobConfigurations(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "perPage", required = false, defaultValue = "30") Integer perPage) {
		return new ResponseEntity<>(jobConfigurationService.list(page, perPage), HttpStatus.OK);
	}

	@GetMapping(path = "/configurations/{id}")
	public ResponseEntity<JobConfiguration> getJobConfiguration(@PathVariable Long id) {
		return new ResponseEntity<>(jobConfigurationService.get(id), HttpStatus.OK);
	}

	@PostMapping(path = "/configurations/{id}/run")
	public ResponseEntity<JobConfiguration> runJobConfiguration(@PathVariable Long id) throws JobExecutionException {
		JobConfiguration jobConfiguration = jobConfigurationService.get(id);
		jobLauncher.launch(new JobLaunchRequest(jobConfiguration));
		jobConfiguration.setJobStatus(BatchStatus.STARTING);

		return new ResponseEntity<>(jobConfiguration, HttpStatus.OK);
	}

	@RequestMapping(path = "/lists", method = RequestMethod.GET)
	public ResponseEntity<Page<JobList>> listJobLists(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "perPage", required = false, defaultValue = "30") Integer perPage) {
		return new ResponseEntity<>(jobListService.list(page, perPage), HttpStatus.OK);
	}

	@GetMapping(path = "/lists/{id}")
	public ResponseEntity<JobList> getJobList(@PathVariable Long id) {
		return new ResponseEntity<>(jobListService.get(id), HttpStatus.OK);
	}
}
