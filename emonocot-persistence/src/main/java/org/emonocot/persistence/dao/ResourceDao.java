package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.registry.Resource;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
public interface ResourceDao extends SearchableDao<Resource> {
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
    List<Resource> list(String sourceId, Integer page, Integer size);

    /**
     *
     * @param id Set the job id
     * @return the job
     */
    Resource findByJobId(Long id);

	boolean isHarvesting();

	List<Resource> listResourcesToHarvest(Integer limit, DateTime now, String fetch);

	Resource findResourceByUri(String identifier);

}
