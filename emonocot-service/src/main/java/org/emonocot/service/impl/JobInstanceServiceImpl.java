package org.emonocot.service.impl;

import org.emonocot.api.JobInstanceService;
import org.emonocot.persistence.dao.JobInstanceDao;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class JobInstanceServiceImpl implements JobInstanceService {

    /**
     *
     */
    private JobInstanceDao jobInstanceDao;

    /**
     *
     * @param jobInstanceDao
     */
    @Autowired
    public void setJobInstanceDao(JobInstanceDao jobInstanceDao) {
        this.jobInstanceDao = jobInstanceDao;
    }

   /**
    *
    * @param identifier the identifier of the job
    * @return a job instance
    */
    @Transactional(readOnly = true)
    public JobInstance find(long identifier) {
        return jobInstanceDao.load(identifier);
    }

   /**
    *
    * @param id The id to delete
    */
    @Transactional
    public void delete(long id) {
        jobInstanceDao.delete(id);
    }

   /**
    *
    * @param jobInstance The job instance to save
    */
    @Transactional
    public void save(JobInstance jobInstance) {
        jobInstanceDao.save(jobInstance);
    }

}
