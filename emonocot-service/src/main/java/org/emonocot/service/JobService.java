package org.emonocot.service;

import java.util.List;

import org.emonocot.persistence.dao.OlapResult;
import org.springframework.batch.core.JobExecution;

/**
 *
 * @author ben
 *
 */
public interface JobService {

    /**
     *
     * @param identifier Set the authority identifier
     * @param pageSize Set the page size
     * @param pageNumber Set the page number
     * @return a list of JobExecutions
     */
    List<JobExecution> listJobExecutions(String identifier, Integer pageSize,
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
