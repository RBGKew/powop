package org.powo.harvest.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.powo.api.JobConfigurationService;
import org.powo.api.JobListService;
import org.powo.api.OrganisationService;
import org.powo.harvest.export.mixins.JobConfigurationExportMixin;
import org.powo.harvest.export.mixins.JobListExportMixin;
import org.powo.harvest.export.mixins.OrganisationExportMixin;
import org.powo.harvest.export.mixins.ResourceExportMixin;
import org.powo.model.ConfigurationExport;
import org.powo.model.JobConfiguration;
import org.powo.model.JobList;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping(path = "/api/1/data")
public class DataReloadController {

	private final PrettyPrinter printer = new DefaultPrettyPrinter()
			.withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
			.withoutSpacesInObjectEntries();

	private final ObjectMapper mapper = new ObjectMapper()
			.addMixIn(JobConfiguration.class, JobConfigurationExportMixin.class)
			.addMixIn(JobList.class, JobListExportMixin.class)
			.addMixIn(Organisation.class, OrganisationExportMixin.class)
			.addMixIn(Resource.class, ResourceExportMixin.class)
			.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS , true);

	@Autowired
	private OrganisationService organisationService;

	@Autowired
	private JobConfigurationService jobService;

	@Autowired
	private JobListService jobListService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void exportConfiguration(HttpServletResponse response) throws IOException {
		ConfigurationExport export = ConfigurationExport.builder()
				.organisations(organisationService.list())
				.jobConfigurations(jobService.list())
				.jobLists(jobListService.list())
				.build();

		String conf = mapper.writer(printer).writeValueAsString(export);
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"powo-configuration.json\"");
		response.addHeader("Access-Control-Allow-Origin", "*" );
		response.setContentLength(conf.length());

		response.getWriter().print(conf);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> importConfiguration(@RequestBody ConfigurationExport conf, BindingResult result) throws JsonProcessingException {
		try {
			for(Organisation organisation : conf.getOrganisations()) {
				// jackson doesn't set back references when deserialising so it must be done manually
				organisation.getResources().forEach(resource -> resource.setOrganisation(organisation));
				organisationService.save(organisation);
			}

			conf.getJobConfigurations().forEach(jobService::save);

			for(JobList jobList : conf.getJobLists()) {
				List<JobConfiguration> jobConfigurations = jobList.getJobConfigurations().stream()
						.filter(Objects::nonNull)
						.map(jc -> jobService.find(jc.getIdentifier()))
						.collect(Collectors.toList());
				jobList.setJobConfigurations(jobConfigurations);
				jobListService.save(jobList);
			}

			return new ResponseEntity<>(mapper.writeValueAsString(conf), HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			String response = "{\"error\":\"Configuration already loaded into database. This is designed to load an empty database\"}";
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
}
