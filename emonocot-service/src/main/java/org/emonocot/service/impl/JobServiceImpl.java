package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.JobService;
import org.emonocot.model.job.Job;
import org.emonocot.persistence.dao.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public final List<Job> list(final String sourceId, final Integer page,
            final Integer size) {
        return dao.list(sourceId, page, size);
    }

}
