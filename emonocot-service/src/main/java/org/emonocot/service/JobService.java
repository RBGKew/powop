package org.emonocot.service;

import java.util.List;

import org.springframework.batch.core.JobExecution;

public interface JobService {

    /**
     *
     * @param identifier Set the authority identifier
     * @param pageSize Set the page size
     * @return a list of JobExecutions
     */
    List<JobExecution> listJobExecutions(String identifier, Integer pageSize);

}
