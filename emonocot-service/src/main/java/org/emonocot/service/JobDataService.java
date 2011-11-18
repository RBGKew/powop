package org.emonocot.service;

import java.util.List;

import org.emonocot.api.JobExecutionService;
import org.emonocot.persistence.dao.OlapResult;

/**
 *
 * @author ben
 *
 */
public interface JobDataService extends JobExecutionService {

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

}
