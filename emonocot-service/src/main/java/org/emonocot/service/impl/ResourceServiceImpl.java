package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.ResourceService;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.dao.ResourceDao;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class ResourceServiceImpl extends SearchableServiceImpl<Resource, ResourceDao> implements
        ResourceService {
    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    /**
    *
    * @param newJobDao Set the image dao
    */
   @Autowired
   public final void setJobDao(final ResourceDao newJobDao) {
       super.dao = newJobDao;
   }

   /**
    * @param sourceId Set the source identifier
    * @return the total number of jobs for a given source
    */
    @Transactional(readOnly = true)
    public final Long count(final String sourceId) {
        return dao.count(sourceId);
    }

    /**
     * @param sourceId
     *            Set the source identifier
     * @param page
     *            Set the offset (in size chunks, 0-based), optional
     * @param size
     *            Set the page size
     * @return A list of jobs
     */
    @Transactional(readOnly = true)
    public final List<Resource> list(final String sourceId, final Integer page,
            final Integer size) {
        return dao.list(sourceId, page, size);
    }

    /**
     * @param id Set the job id
     * @return the job
     */
    @Transactional(readOnly = true)
    public final Resource findByJobId(final Long id) {
        return dao.findByJobId(id);
    }

    @Transactional(readOnly = true)
	public boolean isHarvesting() {
		return dao.isHarvesting();
	}

    @Transactional(readOnly = true)
	public List<Resource> listResourcesToHarvest(Integer limit, DateTime now) {
    	return dao.listResourcesToHarvest(limit,now);
	}
}
