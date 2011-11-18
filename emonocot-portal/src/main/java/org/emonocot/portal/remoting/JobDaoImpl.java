package org.emonocot.portal.remoting;

import java.util.List;

import org.emonocot.persistence.dao.JobExecutionDao;
import org.emonocot.persistence.dao.OlapResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ben
 *
 */
@Repository
public class JobDaoImpl implements JobExecutionDao {

    /**
     * Logger.
     */
    private static Logger logger = LoggerFactory.getLogger(JobDaoImpl.class);

    /**
    *
    */
    private String baseUri;

    /**
    *
    */
    private String resourceDir;

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

    public List<JobExecution> getJobExecutions(String authorityName,
            Integer pageSize, Integer pageNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<OlapResult> countObjects(Long jobExecutionId) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<OlapResult> countErrors(Long jobExecutionId, String objectType) {
        // TODO Auto-generated method stub
        return null;
    }

    public JobExecution load(Long identifier) {
        return restTemplate.getForObject(baseUri + resourceDir + "/"
                + identifier, JobExecution.class);
    }

    public void delete(long id) {
        restTemplate.delete(baseUri + resourceDir + "/" + id);
    }

    public void save(JobExecution jobExecution) {
        logger.debug("POST: " + baseUri + resourceDir);
        restTemplate.postForObject(baseUri + resourceDir, jobExecution, JobExecution.class);
    }

}
