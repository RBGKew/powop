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

import org.emonocot.api.JobInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
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

/**
 *
 * @author ben
 *
 */
@Controller
public class JobInstanceController {

    /**
    *
    */
   private static Logger logger = LoggerFactory
           .getLogger(JobInstanceController.class);

    /**
     *
     */
    private JobInstanceService service;

   /**
    *
    */
   private String baseUrl;

   /**
    *
    * @param newBaseUrl Set the base url
    */
   public final void setBaseUrl(final String newBaseUrl) {
       this.baseUrl = newBaseUrl;
   }

    /**
     * @param service set the job instance service
     */
    @Autowired
    public final void setInstanceService(JobInstanceService service) {
        this.service = service;
    }
    
    @RequestMapping(value = "/jobInstance",
    		method = RequestMethod.GET,
            consumes = "application/json",
            produces = "application/json")
    public final ResponseEntity<List<JobInstance>> list(@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
    		                                  @RequestParam(value = "start", required = false, defaultValue = "0") Integer start) {
        return new ResponseEntity<List<JobInstance>>(service.list(limit, start), HttpStatus.OK);
    }

      /**
       * @param identifier
       *            Set the identifier of the group
       * @return A model and view containing a group
       */
      @RequestMapping(value = "/jobInstance/{identifier}",
                      method = RequestMethod.GET,
                      produces = "application/json")
    public final ResponseEntity<JobInstance> get(
            @PathVariable final Long identifier) {
        return new ResponseEntity<JobInstance>(service.find(identifier),
                HttpStatus.OK);
    }

      /**
       * @param jobInstance
       *            the job instance to save
       * @return A response entity containing a newly created job instance
       */
        @RequestMapping(value = "/jobInstance",
                        method = RequestMethod.POST)
      public final ResponseEntity<JobInstance> create(@RequestBody final JobInstance jobInstance) {
          HttpHeaders httpHeaders = new HttpHeaders();
          try {
              httpHeaders.setLocation(new URI(baseUrl + "/jobInstance/"
                      + jobInstance.getId()));
          } catch (URISyntaxException e) {
              logger.error(e.getMessage());
          }
          service.save(jobInstance);
          ResponseEntity<JobInstance> response = new ResponseEntity<JobInstance>(
                  jobInstance, httpHeaders, HttpStatus.CREATED);
          return response;
      }

      /**
       * @param identifier
       *            Set the identifier of the jobInstance
       * @return A response entity containing the status
       */
        @RequestMapping(value = "/jobInstance/{identifier}",
                        method = RequestMethod.DELETE)
        public final ResponseEntity<JobInstance> delete(
                @PathVariable final Long identifier) {
            service.delete(identifier);
            return new ResponseEntity<JobInstance>(HttpStatus.OK);
        }
}
