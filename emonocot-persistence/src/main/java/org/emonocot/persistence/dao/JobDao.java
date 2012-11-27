package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.Job;

/**
 *
 * @author ben
 *
 */
public interface JobDao extends Dao<Job> {
    /**
     * @param sourceId Set the source identifier
     * @return the total number of jobs for a given source
     */
    Long count(String sourceId);

    /**
     * @param sourceId Set the source identifier
     * @param page Set the offset (in size chunks, 0-based), optional
     * @param size Set the page size
     * @return A list of jobs
     */
    List<Job> list(String sourceId, Integer page, Integer size);

    /**
     *
     * @param id Set the job id
     * @return the job
     */
    Job findByJobId(Long id);

}
