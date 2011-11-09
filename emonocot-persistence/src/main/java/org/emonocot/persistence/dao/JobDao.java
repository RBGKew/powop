package org.emonocot.persistence.dao;

import java.util.List;

import org.springframework.batch.core.JobExecution;

/**
 *
 * @author ben
 *
 */
public interface JobDao {

    /**
     *
     * @param authorityName the name of the authorty
     * @param pageSize set the maximum size of the list of executions
     * @param pageNumber Set the page number
     * @return a list of job executions
     */
    List<JobExecution> getJobExecutions(String authorityName, Integer pageSize,
            Integer pageNumber);

    /**
     *
     * @param jobExecutionId Set the job execution identifier
     * @return a result object
     */
    List<OlapResult> countObjects(Long jobExecutionId);

    /**
     *
     * @param jobExecutionId Set the job execution identifier
     * @param objectType set the object type
     * @return a result object
     */
    List<OlapResult> countErrors(Long jobExecutionId, String objectType);

   /**
    *
    * @param identifier the identifier of the job
    * @return a job execution
    */
    JobExecution load(Long identifier);

}
