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

import javax.servlet.http.HttpServletRequest;

import org.emonocot.api.Service;
import org.emonocot.model.Base;
import org.emonocot.pager.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateObjectRetrievalFailureException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 *
 * @author ben
 * @param <T> the type of object served by this controller
 * @param <SERVICE> the service supplying this object
 */
public abstract class GenericController<T extends Base,
SERVICE extends Service<T>> {

	private static Logger logger = LoggerFactory.getLogger(GenericController.class);

	private SERVICE service;

	private ObjectMapper objectMapper;

	private String directory;

	private Class<T> type;

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public GenericController(String directory, Class<T> type) {
		this.directory = directory;
		this.type = type;
	}

	private String getDirectory() {
		return directory;
	}

	@RequestMapping(value = "/{identifier}",
			method = RequestMethod.GET,
			consumes = "application/json",
			produces = "application/json")
	public ResponseEntity<T> get(@PathVariable String identifier, @RequestParam(value = "fetch", required = false) String fetch) {
		return new ResponseEntity<T>(service.find(identifier,fetch), HttpStatus.OK);
	}

	@RequestMapping(value = "/{identifier}",
			params = "callback",
			method = RequestMethod.GET,
			produces = "application/javascript")
	public ResponseEntity<JSONPObject> getJsonP(@PathVariable String identifier, @RequestParam(value = "fetch", required = false) String fetch,
			@RequestParam(value = "callback", required = true) String callback) {
		return new ResponseEntity<JSONPObject>(new JSONPObject(callback,service.find(identifier,fetch)), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET,
			consumes = "application/json",
			produces = "application/json")
	public ResponseEntity<Page<T>> list(@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start) {
		return new ResponseEntity<Page<T>>(service.list(start, limit, null), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST,
			produces = "application/json",
			consumes = "application/json")
	public ResponseEntity<T> create(@RequestBody T object, UriComponentsBuilder builder) throws Exception {

		try {
			service.merge(object);
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage());
			for(StackTraceElement ste : e.getStackTrace()) {
				logger.error(ste.toString());
			}
			throw e;
		}

		T persistedObject = service.find(object.getIdentifier());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/" + getDirectory() + "/{id}").buildAndExpand(persistedObject.getId()).toUri());

		return new ResponseEntity<T>(object, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}",
			method = RequestMethod.DELETE,
			consumes = "application/json",
			produces = "application/json")
	public ResponseEntity<T> delete(@PathVariable Long id) {
		service.deleteById(id);
		return new ResponseEntity<T>(HttpStatus.OK);
	}

	public void setService(SERVICE newService) {
		this.service = newService;
	}

	public SERVICE getService() {
		return service;
	}

	@ExceptionHandler(HibernateObjectRetrievalFailureException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ModelAndView handleObjectNotFoundException(HibernateObjectRetrievalFailureException orfe) {
		ModelAndView modelAndView = new ModelAndView("not_found_error");
		modelAndView.addObject("exception", orfe);
		return modelAndView;
	}

	protected void populateCsrfToken(HttpServletRequest request, Model model) {
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		model.addAttribute("_csrf", csrfToken);
	}
}
