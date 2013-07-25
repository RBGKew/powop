package org.emonocot.api;

import java.util.List;

import org.emonocot.api.job.CouldNotLaunchJobException;
import org.emonocot.api.job.ResourceAlreadyBeingHarvestedException;
import org.emonocot.model.registry.Resource;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
public interface ResourceService extends SearchableService<Resource> {
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
     * @return a matching job
     */
    Resource findByJobId(Long id);

	boolean isHarvesting();

	List<Resource> listResourcesToHarvest(Integer limit, DateTime now, String fetch);
	
	void harvestResource(Long resourceId, Boolean ifModified) throws ResourceAlreadyBeingHarvestedException, CouldNotLaunchJobException;

	Resource findByResourceUri(String identifier) throws ResourceAlreadyBeingHarvestedException, CouldNotLaunchJobException;
}
