package org.emonocot.portal.remoting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.persistence.dao.JobInstanceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
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
public class JobInstanceDaoImpl implements JobInstanceDao {

    /**
     * Logger.
     */
    private static Logger logger = LoggerFactory
            .getLogger(JobInstanceDaoImpl.class);
    
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
    private String resourceDir = "jobInstance";

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

    /**
     * @param identifier the identifier of the job instance to load
     * @return a job instance
     */
    public final JobInstance load(final Long identifier) {
        return restTemplate.getForObject(baseUri + "/"  + resourceDir + "/"
                + identifier, JobInstance.class);
    }

    /**
     * @param id the id of the job instance to delete
     */
    public final void delete(final Long id) {
        restTemplate.delete(baseUri + "/" + resourceDir + "/" + id);
    }

    /**
     * @param jobInstance the job execution to save
     */
    public final void save(final JobInstance jobInstance) {
        logger.debug("POST: " + baseUri + "/"+ resourceDir);
        restTemplate.postForObject(baseUri+ "/" + resourceDir, jobInstance,
                JobInstance.class);
    }

	@Override
	public List<JobInstance> list(Integer page, Integer size) {
		HttpEntity<JobInstance> requestEntity = new HttpEntity<JobInstance>(httpHeaders);
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
    	
    	
    	ParameterizedTypeReference<List<JobInstance>> typeRef = new ParameterizedTypeReference<List<JobInstance>>() {};
        HttpEntity<List<JobInstance>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
	}

}
