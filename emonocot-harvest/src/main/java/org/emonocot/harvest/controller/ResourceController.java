/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.harvest.controller;

import javax.validation.Valid;
import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobConfigurationException;
import org.emonocot.factories.JobConfigurationFactory;
import org.emonocot.model.marshall.json.ResourceWithJob;
import org.emonocot.model.registry.Resource;
import org.emonocot.pager.Page;
import org.emonocot.service.impl.JobConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/1/resource", produces = "application/json")
public class ResourceController {
	private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private JobConfigurationService jobConfigurationService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Resource>> list(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "perPage", required = false, defaultValue = "30") Integer perPage) {
		return new ResponseEntity<>(resourceService.list(page, perPage, "job-with-source"), HttpStatus.OK);
	}

	@GetMapping("/{resourceId}")
	public ResponseEntity<Resource> show(@PathVariable Long resourceId) {
		return new ResponseEntity<>(resourceService.load(resourceId,"job-with-source"), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Resource> create(
			@Valid @RequestBody Resource resource,
			BindingResult result) {

		logger.debug("Creating {}", resource);
		resourceService.save(resource);
		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	@PostMapping("/withJob")
	public ResponseEntity<ResourceWithJob> createWithJob(
			@Valid @RequestBody ResourceWithJob resourceWithJob,
			BindingResult result) throws JobConfigurationException {

		resourceService.save(resourceWithJob.getResource());
		resourceWithJob.setJobConfiguration(JobConfigurationFactory.resourceJob(resourceWithJob));
		jobConfigurationService.save(resourceWithJob.getJobConfiguration());

		return new ResponseEntity<>(resourceWithJob, HttpStatus.CREATED);
	}

	@PostMapping("/{resourceId}")
	public ResponseEntity<Resource> update(
			@PathVariable Long resourceId,
			@Valid @RequestBody Resource resource,
			BindingResult result) {

		Resource persistedResource = resourceService.load(resourceId);

		if (result.hasErrors()) {
			for(ObjectError objectError : result.getAllErrors()) {
				logger.error(objectError.getDefaultMessage());
			}
		}

		logger.debug("Updating " + resource);
		resourceService.saveOrUpdate(persistedResource);
		return new ResponseEntity<>(persistedResource, HttpStatus.OK);
	}
}