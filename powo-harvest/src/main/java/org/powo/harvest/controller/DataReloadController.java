package org.powo.harvest.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.powo.api.JobConfigurationService;
import org.powo.api.JobListService;
import org.powo.api.OrganisationService;
import org.powo.harvest.export.mixins.JobConfigurationExportMixin;
import org.powo.harvest.export.mixins.JobListExportMixin;
import org.powo.harvest.export.mixins.ResourceExportMixin;
import org.powo.model.ConfigurationExport;
import org.powo.model.JobConfiguration;
import org.powo.model.JobList;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/1/data")
public class DataReloadController {

	private final ObjectMapper mapper = new ObjectMapper()
			.addMixIn(JobConfiguration.class, JobConfigurationExportMixin.class)
			.addMixIn(JobList.class, JobListExportMixin.class)
			.addMixIn(Resource.class, ResourceExportMixin.class);

	@Autowired
	private OrganisationService organisationService;

	@Autowired
	private JobConfigurationService jobService;

	@Autowired
	private JobListService jobListService;

	@GetMapping(produces = "application/json; charset=utf-8")
	public ResponseEntity<String> exportConfiguration() throws JsonProcessingException {
		ConfigurationExport export = ConfigurationExport.builder()
				.organisations(organisationService.list("resources"))
				.jobConfigurations(jobService.list())
				.jobLists(jobListService.list())
				.build();

		return new ResponseEntity<>(mapper.writeValueAsString(export), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<String> importConfiguration(@RequestBody ConfigurationExport conf, BindingResult result) throws JsonProcessingException {
		try {
			for(Organisation organisation : conf.getOrganisations()) {
				// jackson doesn't set back references when deserialising so it must be done manually
				organisation.getResources().forEach(resource -> resource.setOrganisation(organisation));
				organisationService.saveOrUpdate(organisation);
			}

			conf.getJobConfigurations().forEach(jobService::saveOrUpdate);

			for(JobList jobList : conf.getJobLists()) {
				List<JobConfiguration> jobConfigurations = jobList.getJobConfigurations().stream()
						.filter(Objects::nonNull)
						.map(jc -> jobService.get(jc.getIdentifier()))
						.collect(Collectors.toList());
				jobList.setJobConfigurations(jobConfigurations);
				jobListService.saveOrUpdate(jobList);
			}

			return new ResponseEntity<>(mapper.writeValueAsString(conf), HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			String response = "{\"error\":\"Configuration already loaded into database. This is designed to load an empty database\"}";
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}
