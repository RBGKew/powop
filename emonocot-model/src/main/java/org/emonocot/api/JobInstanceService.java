package org.emonocot.api;

import java.util.List;

import org.springframework.batch.core.JobInstance;

/**
 *
 * @author ben
 *
 */
public interface JobInstanceService {

    /**
     *
     * @param id Set the id
     * @return a job instance
     */
    JobInstance find(long id);

    /**
     *
     * @param id The id to delete
     */
    void delete(long id);

    /**
     *
     * @param jobInstance The jobInstance to save
     */
    void save(JobInstance jobInstance);

    /**
     *
     * @param limit
     * @param start
     * @return
     */
	List<JobInstance> list(Integer limit, Integer start);

}
