package org.emonocot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.emonocot.api.ResourceService;
import org.emonocot.api.job.CouldNotLaunchJobException;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.api.job.ResourceAlreadyBeingHarvestedException;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.dao.ResourceDao;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
    
    private static BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1,	9, 0, 0, 0);
    
	private JobLauncher jobLauncher;

    /**
    *
    * @param newJobDao Set the image dao
    */
   @Autowired
   public final void setJobDao(final ResourceDao newJobDao) {
       super.dao = newJobDao;
   }
   
    @Autowired
	@Qualifier("messageBasedReadWriteJobLauncher")
	public void setJobLauncher(JobLauncher jobLauncher) {
	   this.jobLauncher = jobLauncher;
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
	public List<Resource> listResourcesToHarvest(Integer limit, DateTime now, String fetch) {
    	return dao.listResourcesToHarvest(limit,now,fetch);
	}

	@Override
	@Transactional(readOnly = false)
	public void harvestResource(Long resourceId, Boolean ifModified)
			throws ResourceAlreadyBeingHarvestedException, CouldNotLaunchJobException {
		Resource resource = load(resourceId,"job-with-source");
		if (resource.getStatus() != null) {
			switch (resource.getStatus()) {
			case STARTED:
			case STARTING:
			case STOPPING:
			case UNKNOWN:
				throw new ResourceAlreadyBeingHarvestedException();				
			case COMPLETED:
			case FAILED:
			case STOPPED:
			case ABANDONED:
			default:
				break;
			}
		}
		Map<String, String> jobParametersMap = new HashMap<String, String>();
		jobParametersMap.put("authority.name", resource.getOrganisation().getIdentifier());
		jobParametersMap.put("attempt", UUID.randomUUID().toString()); // Prevent jobs failing if a job has been executed with the same parameters
		jobParametersMap.put("authority.uri", resource.getUri());
		jobParametersMap.put("resource.identifier", resource.getIdentifier());
        jobParametersMap.put("skip.unmodified", ifModified.toString());

		if (resource.getStatus() == null || !ifModified || resource.getStartTime() == null) {
			jobParametersMap.put("timestamp", Long.toString(System.currentTimeMillis()));
			jobParametersMap.put("authority.last.harvested", Long.toString((PAST_DATETIME.getMillis())));
		} else {
			jobParametersMap.put("authority.last.harvested",Long.toString((resource.getStartTime().getMillis())));
		}
		
		jobParametersMap.putAll(resource.getParameters());

		JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
		jobLaunchRequest.setJob(resource.getResourceType().getJobName());		
		jobLaunchRequest.setParameters(jobParametersMap);

		try {
			jobLauncher.launch(jobLaunchRequest);
			resource.setStartTime(null);
			resource.setLastAttempt(new DateTime());
			resource.setDuration(null);
			resource.setExitCode(null);
			resource.setExitDescription(null);
			resource.setJobId(null);
			resource.setStatus(BatchStatus.UNKNOWN);
			resource.setRecordsRead(0);
			resource.setReadSkip(0);
			resource.setProcessSkip(0);
			resource.setWriteSkip(0);
			resource.setWritten(0);
			saveOrUpdate(resource);
			
		} catch (JobExecutionException e) {
			throw new CouldNotLaunchJobException(e.getMessage());
		}
		
	}

	@Transactional(readOnly = true)
	public Resource findByResourceUri(String identifier) {
		return dao.findResourceByUri(identifier);
	}

	@Override
	@PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
	@Transactional(readOnly = false)
	public void deleteById(Long id) {
		super.deleteById(id);
	}

	@Override
	@PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
	@Transactional(readOnly = false)
	public void delete(String identifier) {
		super.delete(identifier);
	}
	
	
}
