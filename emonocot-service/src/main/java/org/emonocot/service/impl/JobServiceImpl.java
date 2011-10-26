package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.persistence.dao.JobDao;
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
     * @return a list of job executions
     */
    @Transactional(readOnly = true)
    public List<JobExecution> listJobExecutions(String identifier,
            Integer pageSize) {
        return dao.getJobExecutions(identifier, pageSize);
    }

}
