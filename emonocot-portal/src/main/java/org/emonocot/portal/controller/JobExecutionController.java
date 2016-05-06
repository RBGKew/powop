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
package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.emonocot.api.JobExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JobExecutionController {

	private static Logger logger = LoggerFactory.getLogger(JobExecutionController.class);

	private JobExecutionService service;

	private String baseUrl;

	public final void setBaseUrl(final String newBaseUrl) {
		this.baseUrl = newBaseUrl;
	}

	@Autowired
	public final void setJobExecutionService(JobExecutionService service) {
		this.service = service;
	}

	@RequestMapping(value = "/jobExecution",
			method = RequestMethod.GET,
			consumes = "application/json",
			produces = "application/json")
	public final ResponseEntity<List<JobExecution>> list(@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "authority", required = false) String authority) {
		return new ResponseEntity<List<JobExecution>>(service.listJobExecutions(authority, limit, start), HttpStatus.OK);
	}

	@RequestMapping(value = "/jobExecution/{identifier}", method = RequestMethod.GET, produces = "application/json")
	public final ResponseEntity<JobExecution> get(
			@PathVariable final Long identifier) {
		return new ResponseEntity<JobExecution>(service.find(identifier), HttpStatus.OK);
	}

	@RequestMapping(value = "/jobExecution", method = RequestMethod.POST)
	public final ResponseEntity<JobExecution> create(
			@RequestBody final JobExecution jobExecution) {
		HttpHeaders httpHeaders = new HttpHeaders();
		try {
			httpHeaders.setLocation(new URI(baseUrl + "/jobExecution/" + jobExecution.getId()));
		} catch (URISyntaxException e) {
			logger.error(e.getMessage());
		}
		service.save(jobExecution);
		ResponseEntity<JobExecution> response = new ResponseEntity<JobExecution>(jobExecution, httpHeaders, HttpStatus.CREATED);
		return response;
	}

	@RequestMapping(value = "/jobExecution/{identifier}", method = RequestMethod.DELETE)
	public final ResponseEntity<JobExecution> delete(
			@PathVariable final Long identifier) {
		service.delete(identifier);
		return new ResponseEntity<JobExecution>(HttpStatus.OK);
	}
}
