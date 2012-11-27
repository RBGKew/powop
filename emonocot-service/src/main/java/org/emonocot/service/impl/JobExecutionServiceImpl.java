package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.JobExecutionService;
import org.emonocot.persistence.dao.JobExecutionDao;
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
public class JobExecutionServiceImpl implements JobExecutionService {

    /**
     *
     */
    private JobExecutionDao dao;

    /**
     *
     * @param jobExecutionDao
     *            set the job dao
     */
    @Autowired
    public void setJobExecutionDao(JobExecutionDao jobExecutionDao) {
        this.dao = jobExecutionDao;
    }

    /**
     * @param identifier
     *            Set the identifier
     * @param pageSize
     *            Set the page size
     * @param pageNumber
     *            Set the page number
     * @return a list of job executions
     */
    @Transactional(readOnly = true)
    public List<JobExecution> listJobExecutions(final String identifier,
            final Integer pageSize, final Integer pageNumber) {
        return dao.getJobExecutions(identifier, pageSize, pageNumber);
    }

    /**
     *
     * @param identifier
     *            the identifier of the job
     * @return a job execution
     */
    @Transactional(readOnly = true)
    public JobExecution find(final long identifier) {
        return dao.load(identifier);
    }

    /**
     *
     * @param id The id to delete
     */
    @Transactional
    public void delete(final long id) {
        dao.delete(id);
    }

   /**
    *
    * @param jobExecution The jobExecution to save
    */
    @Transactional
    public void save(JobExecution jobExecution) {
        dao.save(jobExecution);
    }
}
