package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.JobService;
import org.emonocot.model.job.Job;
import org.emonocot.persistence.dao.JobDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class JobServiceImpl extends ServiceImpl<Job, JobDao> implements
        JobService {
    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    /**
    *
    * @param newJobDao Set the image dao
    */
   @Autowired
   public final void setJobDao(final JobDao newJobDao) {
       super.dao = newJobDao;
   }

   /**
    * @param sourceId Set the source identifier
    * @return the total number of jobs for a given source
    */
    @Transactional(readOnly = true)
    public final Long count(final String sourceId) {
        return dao.count(sourceId);
    }

    /**
     * @param sourceId
     *            Set the source identifier
     * @param page
     *            Set the offset (in size chunks, 0-based), optional
     * @param size
     *            Set the page size
     * @return A list of jobs
     */
    @Transactional(readOnly = true)
    public final List<Job> list(final String sourceId, final Integer page,
            final Integer size) {
        return dao.list(sourceId, page, size);
    }

    /**
     * @param id Set the job id
     * @return the job
     */
    @Transactional(readOnly = true)
    public final Job findByJobId(final Long id) {
        return dao.findByJobId(id);
    }
}
