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
package org.powo.harvest.controller;

import java.util.List;

import javax.validation.Valid;

import org.powo.api.ResourceService;
import org.powo.model.exception.InvalidEntityException;
import org.powo.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/1/resource", produces = "application/json")
public class ResourceController {

	private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

	@Autowired
	private ResourceService resourceService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Resource>> list() {
		return new ResponseEntity<>(resourceService.list(), HttpStatus.OK);
	}

	@GetMapping("/{identifier}")
	public ResponseEntity<Resource> show(@PathVariable String identifier) {
		return new ResponseEntity<>(resourceService.load(identifier), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Resource> create(
			@Valid @RequestBody Resource resource,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new InvalidEntityException(Resource.class, result);
		}

		logger.debug("Creating " + resource);
		resourceService.save(resource);

		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	@PostMapping("/{identifier}")
	public ResponseEntity<Resource> update(
			@PathVariable String identifier,
			@Valid @RequestBody Resource resource,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new InvalidEntityException(Resource.class, result);
		}

		logger.debug("Updating " + resource);
		resourceService.saveOrUpdate(resource);
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@DeleteMapping("/{identifier}")
	public ResponseEntity<Resource> delete(@PathVariable String identifier) {
		Resource resource = resourceService.find(identifier);
		resourceService.delete(identifier);
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}