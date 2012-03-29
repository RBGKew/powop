package org.emonocot.api;

import java.util.List;

import org.emonocot.model.job.Job;

/**
 *
 * @author ben
 *
 */
public interface JobService extends Service<Job> {
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
     * @return a matching job
     */
    Job findByJobId(Long id);
}
