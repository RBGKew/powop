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
package org.emonocot.portal.remoting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.emonocot.persistence.dao.JobExecutionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ben
 *
 */
@Repository
public class JobExecutionDaoImpl implements JobExecutionDao {

    /**
     * Logger.
     */
    private static Logger logger = LoggerFactory
            .getLogger(JobExecutionDaoImpl.class);
    
    protected static HttpHeaders httpHeaders = new HttpHeaders();

    static {
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptableMediaTypes);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    /**
    *
    */
    private String baseUri;

    /**
    *
    */
    private String resourceDir = "jobExecution";

    /**
     *
     */
    private RestTemplate restTemplate;

    /**
     *
     */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * @param newBaseUri
     *            Set the base uri
     */
    public final void setBaseUri(final String newBaseUri) {
        this.baseUri = newBaseUri;
    }

    public List<JobExecution> getJobExecutions(String authority, Integer size, Integer page) {
    	HttpEntity<JobExecution> requestEntity = new HttpEntity<JobExecution>(httpHeaders);
    	Map<String,Object> uriVariables = new HashMap<String,Object>();
    	uriVariables.put("resource", resourceDir);
    	if(size == null) {
    		uriVariables.put("limit", "");
    	} else {
    		uriVariables.put("limit", size);
    	}
    	
    	if(page == null) {
    		uriVariables.put("start", "");
    	} else {
    		uriVariables.put("start", page);
    	}
    	
    	if(page == null) {
    		uriVariables.put("authority", "");
    	} else {
    		uriVariables.put("authority", authority);
    	}
    	
    	ParameterizedTypeReference<List<JobExecution>> typeRef = new ParameterizedTypeReference<List<JobExecution>>() {};
        HttpEntity<List<JobExecution>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}&authority={authority}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
    }


    /**
     * @param identifier the identifier of the job execution to load
     * @return a job execution
     */
    public final JobExecution load(final Long identifier) {
        return restTemplate.getForObject(baseUri + "/" + resourceDir + "/"
                + identifier, JobExecution.class);
    }

    /**
     * @param id the id of the job execution to delete
     */
    public final void delete(final Long id) {
        restTemplate.delete(baseUri + "/" + resourceDir + "/" + id);
    }

    /**
     * @param jobExecution the job execution to save
     */
    public final void save(final JobExecution jobExecution) {
        logger.debug("POST: "+ "/" + baseUri + resourceDir);
        restTemplate.postForObject(baseUri+ "/" + resourceDir, jobExecution,
                JobExecution.class);
    }
}
