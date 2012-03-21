package org.emonocot.portal.remoting;

import java.util.List;

import org.emonocot.model.job.Job;
import org.emonocot.persistence.dao.JobDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class JobDaoImpl extends DaoImpl<Job> implements JobDao {

    /**
     *
     */
    public JobDaoImpl() {
        super(Job.class, "job");
    }

    /**
     * @param sourceId Set the source identifier
     * @return the total number of jobs for a given source
     */
    public Long count(String sourceId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param sourceId Set the source identifier
     * @param page Set the offset (in size chunks, 0-based), optional
     * @param size Set the page size
     * @return A list of jobs
     */
    public List<Job> list(String sourceId, Integer page, Integer size) {
        // TODO Auto-generated method stub
        return null;
    }

}
