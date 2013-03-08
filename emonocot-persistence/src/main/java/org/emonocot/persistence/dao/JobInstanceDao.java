package org.emonocot.persistence.dao;

import java.util.List;

import org.springframework.batch.core.JobInstance;

/**
 *
 * @author ben
 *
 */
public interface JobInstanceDao {

   /**
    *
    * @param identifier the identifier of the job
    * @return a job instance
    */
    JobInstance load(Long identifier);

    /**
     *
     * @param id The id to delete
     */
    void delete(Long id);

    /**
    *
    * @param jobInstance The jobInstance to save
    */
    void save(JobInstance jobInstance);
    
    List<JobInstance> list(Integer page, Integer size);
}
