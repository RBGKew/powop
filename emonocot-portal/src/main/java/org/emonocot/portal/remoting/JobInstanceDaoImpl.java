package org.emonocot.portal.remoting;

import org.emonocot.persistence.dao.JobInstanceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
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
        return restTemplate.getForObject(baseUri  + resourceDir + "/"
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

}
