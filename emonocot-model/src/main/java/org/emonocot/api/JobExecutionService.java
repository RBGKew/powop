package org.emonocot.api;

import java.util.List;

import org.springframework.batch.core.JobExecution;

/**
 *
 * @author ben
 *
 */
public interface JobExecutionService {

   /**
    *
    * @param id Set the id
    * @return a job execution
    */
   JobExecution find(long id);

   /**
    *
    * @param id The id to delete
    */
   void delete(long id);

   /**
    *
    * @param jobExecution The jobExecution to save
    */
   void save(JobExecution jobExecution);

   /**
   *
   * @param identifier Set the authority identifier
   * @param pageSize Set the page size
   * @param pageNumber Set the page number
   * @return a list of JobExecutions
   */
  List<JobExecution> listJobExecutions(String identifier, Integer pageSize,
          Integer pageNumber);
}
