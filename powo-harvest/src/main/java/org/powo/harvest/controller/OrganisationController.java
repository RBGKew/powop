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

import javax.validation.Valid;

import org.powo.api.OrganisationService;
import org.powo.model.exception.InvalidEntityException;
import org.powo.model.registry.Organisation;
import org.powo.pager.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping(path = "/api/1/organisation", produces = "application/json")
public class OrganisationController {

	private static Logger logger = LoggerFactory.getLogger(OrganisationController.class);

	@Autowired
	private OrganisationService organisationService;

	@GetMapping
	public ResponseEntity<Page<Organisation>> list(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "perPage", required = false, defaultValue = "30") Integer perPage) {
		return new ResponseEntity<>(organisationService.list(page, perPage), HttpStatus.OK);
	}

	@GetMapping(value = "/{identifier}")
	public ResponseEntity<Organisation> show(@PathVariable String identifier) {
		return new ResponseEntity<>(organisationService.load(identifier), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Organisation> create(
			@Valid @RequestBody Organisation organisation,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new InvalidEntityException(Organisation.class, result);
		}

		logger.debug("Creating " + organisation);
		organisationService.saveOrUpdate(organisation);
		return new ResponseEntity<>(organisation, HttpStatus.CREATED);
	}

	@PostMapping(value = "/{identifier}")
	public ResponseEntity<Organisation> update(
			@Valid @RequestBody Organisation organisation,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new InvalidEntityException(Organisation.class, result);
		}

		logger.debug("Updating " + organisation);
		organisationService.saveOrUpdate(organisation);
		return new ResponseEntity<>(organisation, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{identifier}")
	public ResponseEntity<Organisation> delete(@PathVariable String identifier, RedirectAttributes redirectAttributes) {
		Organisation organisation = organisationService.find(identifier);
		organisationService.delete(identifier);

		logger.debug("Deleting " + organisation);
		return new ResponseEntity<>(organisation, HttpStatus.OK);
	}
}
