/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.JobInstanceService;
import org.emonocot.persistence.dao.JobInstanceDao;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class JobInstanceServiceImpl implements JobInstanceService {

    /**
     *
     */
    private JobInstanceDao jobInstanceDao;

    /**
     *
     * @param jobInstanceDao
     */
    @Autowired
    public void setJobInstanceDao(JobInstanceDao jobInstanceDao) {
        this.jobInstanceDao = jobInstanceDao;
    }

   /**
    *
    * @param identifier the identifier of the job
    * @return a job instance
    */
    @Transactional(readOnly = true)
    public JobInstance find(long identifier) {
        return jobInstanceDao.load(identifier);
    }

   /**
    *
    * @param id The id to delete
    */
    @Transactional
    public void delete(long id) {
        jobInstanceDao.delete(id);
    }

   /**
    *
    * @param jobInstance The job instance to save
    */
    @Transactional
    public void save(JobInstance jobInstance) {
        jobInstanceDao.save(jobInstance);
    }

    /**
     *
     */
    @Transactional
    public List<JobInstance> list(Integer limit, Integer start) {
	    return jobInstanceDao.list(start, limit);
    }

}
