package org.emonocot.harvest.controller;

import javax.validation.Valid;

import org.emonocot.api.JobListService;
import org.emonocot.model.JobList;
import org.emonocot.model.exception.InvalidEntityException;
import org.emonocot.model.marshall.json.JobSchedule;
import org.emonocot.model.validators.JobListValidator;
import org.emonocot.pager.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/1/job/list", produces = "application/json")
public class JobListController {

	Logger logger = LoggerFactory.getLogger(JobListController.class);

	@Autowired
	private JobListService jobListService;

	@Autowired
	private JobListValidator validator;

	@GetMapping
	public ResponseEntity<Page<JobList>> listJobLists(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "perPage", required = false, defaultValue = "30") Integer perPage) {
		return new ResponseEntity<>(jobListService.list(page, perPage), HttpStatus.OK);
	}

	@PostMapping
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

	@GetMapping(path = "/{id}")
	public ResponseEntity<JobList> getJobList(@PathVariable Long id) {
		return new ResponseEntity<>(jobListService.get(id), HttpStatus.OK);
	}

	@PostMapping(path = "/{id}")
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

	@DeleteMapping(path= "/{id}")
	public ResponseEntity<String> deleteJobList(@PathVariable Long id) {
		jobListService.delete(id);

		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@PostMapping(path = "/{id}/schedule")
	public ResponseEntity<JobList> scheduleJobList(
			@PathVariable Long id,
			@RequestBody JobSchedule schedule) {

		return new ResponseEntity<>(jobListService.schedule(id, schedule), HttpStatus.OK);
	}

}
