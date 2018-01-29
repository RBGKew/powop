package org.emonocot.harvest.controller;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.factories.JobConfigurationFactory;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.constants.JobType;
import org.emonocot.model.constants.SchedulingPeriod;
import org.emonocot.model.exception.InvalidEntityException;
import org.emonocot.model.marshall.json.JobWithParams;
import org.emonocot.pager.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.validation.Valid;

import org.emonocot.api.JobConfigurationService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/1/job", produces = "application/json")
public class JobController {

	Logger logger = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private JobConfigurationService jobConfigurationService;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobExplorer jobExplorer;

	private final ObjectMapper jobExecutionMapper = new ObjectMapper()
			.addMixIn(StepExecution.class, org.emonocot.model.StepExecution.class);

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
			@RequestParam(value = "perPage", required = false, defaultValue = "300") Integer perPage) {
		return new ResponseEntity<>(jobConfigurationService.list(page, perPage), HttpStatus.OK);
	}

	@PostMapping(path = "/configuration")
	public ResponseEntity<JobConfiguration> createJobConfiguration(
			@Valid @RequestBody JobWithParams jobWithParams,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new InvalidEntityException(JobWithParams.class, result);
		}

		JobConfiguration job = JobConfigurationFactory.buildJob(jobWithParams);
		jobConfigurationService.save(job);

		return new ResponseEntity<>(job, HttpStatus.OK);
	}

	@GetMapping(path = "/configuration/{id}")
	public ResponseEntity<JobConfiguration> getJobConfiguration(@PathVariable Long id) {
		return new ResponseEntity<>(jobConfigurationService.get(id), HttpStatus.OK);
	}

	@GetMapping(path = "/configuration/byType/{name}")
	public ResponseEntity<List<JobConfiguration>> getJobConfigurationsByType(@PathVariable String name) {
		return new ResponseEntity<>(jobConfigurationService.listByName(name), HttpStatus.OK);
	}

	/**
	 *  Get the job execution details for the last run of the given job configuration  
	 *  
	 * @param id Id of job configuration
	 */
	@GetMapping(path = "/configuration/{id}/execution")
	public ResponseEntity<String> getJobExecution(@PathVariable Long id) throws JsonProcessingException {
		JobConfiguration job = jobConfigurationService.get(id);
		String response;
		if(job.getLastJobExecution() != null) {
			response = jobExecutionMapper.writeValueAsString(jobExplorer.getJobExecution(job.getLastJobExecution()));
		} else {
			response = "{}";
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/configuration/{id}/run")
	public ResponseEntity<JobConfiguration> runJobConfiguration(@PathVariable Long id) throws JobExecutionException {
		JobConfiguration jobConfiguration = jobConfigurationService.get(id);
		jobLauncher.launch(new JobLaunchRequest(jobConfiguration));
		logger.info("Running {}", jobConfiguration);
		jobConfiguration.setJobStatus(BatchStatus.STARTING);

		return new ResponseEntity<>(jobConfiguration, HttpStatus.OK);
	}
}