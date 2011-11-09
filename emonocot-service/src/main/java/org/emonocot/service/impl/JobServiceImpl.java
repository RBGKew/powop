package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.persistence.dao.JobDao;
import org.emonocot.persistence.dao.OlapResult;
import org.emonocot.service.JobService;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class JobServiceImpl implements JobService {

    /**
     *
     */
    private JobDao dao;

    /**
     *
     * @param jobDao set the job dao
     */
    @Autowired
    public void setJobDao(JobDao jobDao) {
        this.dao = jobDao;
    }

    /**
     * @param identifier Set the identifier
     * @param pageSize Set the page size
     * @param pageNumber Set the page number
     * @return a list of job executions
     */
    @Transactional(readOnly = true)
    public List<JobExecution> listJobExecutions(final String identifier,
            final Integer pageSize, final Integer pageNumber) {
        return dao.getJobExecutions(identifier, pageSize, pageNumber);
    }

    /**
    *
    * @param jobExecutionId Set the job execution identifier
    * @return a result object
    */
    @Transactional(readOnly = true)
    public List<OlapResult> countObjects(final Long jobExecutionId) {
        return dao.countObjects(jobExecutionId);
    }

    /**
    *
    * @param jobExecutionId Set the job execution identifier
    * @param objectType set the object type
    * @return a result object
    */
    @Transactional(readOnly = true)
    public List<OlapResult> countErrors(final Long jobExecutionId, final String objectType) {
        return dao.countErrors(jobExecutionId, objectType);
    }

   /**
    *
    * @param identifier the identifier of the job
    * @return a job execution
    */
    @Transactional(readOnly = true)
    public JobExecution load(final Long identifier) {
        return dao.load(identifier);
    }
}
