package org.emonocot.harvest.common;

import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class JobStatusNotifierImpl implements JobStatusNotifier {

    private static Logger logger = LoggerFactory.getLogger(JobStatusNotifierImpl.class);

    private ResourceService service;
    
    private SolrIndexingListener solrIndexingListener;
    
    @Autowired
    public final void setResourceService(final ResourceService resourceService) {
        this.service = resourceService;
    }
    
    @Autowired
    public void setSolrIndexingListener(SolrIndexingListener solrIndexingListener) {
    	this.solrIndexingListener = solrIndexingListener;
    }

    public final void notify(final JobExecutionInfo jobExecutionInfo) {
        logger.debug("In notify " + jobExecutionInfo.getId());

        Resource resource = service.find(jobExecutionInfo.getResourceIdentifier(),"job-with-source");
		if (resource != null) {
			
			resource.setDuration(new Duration(new DateTime(0), jobExecutionInfo.getDuration()));
			resource.setExitCode(jobExecutionInfo.getExitCode());
			resource.setExitDescription(jobExecutionInfo.getExitDescription());
			if (jobExecutionInfo.getJobInstance() != null) {
				resource.setJobInstance(jobExecutionInfo.getJobInstance());
			}
			resource.setJobId(jobExecutionInfo.getId());
			resource.setBaseUrl(jobExecutionInfo.getBaseUrl());
			resource.setResource(jobExecutionInfo.getResource());
			resource.setStartTime(jobExecutionInfo.getStartTime());
			resource.setStatus(jobExecutionInfo.getStatus());
			resource.setProcessSkip(jobExecutionInfo.getProcessSkip());
			resource.setRecordsRead(jobExecutionInfo.getRecordsRead());
			resource.setReadSkip(jobExecutionInfo.getReadSkip());
			resource.setWriteSkip(jobExecutionInfo.getWriteSkip());
			resource.setWritten(jobExecutionInfo.getWritten());
			switch(resource.getStatus()) {
			case COMPLETED:
				if(resource.getExitCode().equals("COMPLETED")) {
				    resource.setLastHarvestedJobId(jobExecutionInfo.getId());
				    resource.setLastHarvested(jobExecutionInfo.getStartTime());
					resource.updateNextAvailableDate();
				} else if(resource.getExitCode().equals("NOT MODIFIED")) {
					// it is NOT_MODIFIED, so leave the job id as it is, because
					// we don't have any new annotations
					resource.setLastHarvested(jobExecutionInfo.getStartTime());
					resource.updateNextAvailableDate();
				} else if(resource.getExitCode().equals("FAILED")) {
					// The Job failed in a (slightly) controlled manner, but it still failed
					resource.setNextAvailableDate(null);
					resource.setLastHarvestedJobId(jobExecutionInfo.getId());
				}
				
				break;
			case FAILED:
				// It completed on its own, but some part failed
			case ABANDONED:
				// It has been stopped and abandoned manually, and will not be restarted		
				resource.setNextAvailableDate(null);
				resource.setJobId(jobExecutionInfo.getId());
				break;
			case STOPPED:
			default:
			}
			service.saveOrUpdate(resource);
			solrIndexingListener.indexObject(resource);
		}
    }

	@Override
	public void notify(JobExecutionException jobExecutionException, String resourceIdentifier) {
		if(resourceIdentifier != null) {
		    Resource resource = service.find(resourceIdentifier,"job-with-source");
		    resource.setJobId(null);
			resource.setDuration(null);
			resource.setExitCode("FAILED");
			resource.setExitDescription(jobExecutionException.getLocalizedMessage());
			resource.setJobInstance(null);
			resource.setResource(null);
			resource.setStartTime(null);
			resource.setStatus(BatchStatus.FAILED);
			resource.setProcessSkip(0);
			resource.setRecordsRead(0);
			resource.setReadSkip(0);
			resource.setWriteSkip(0);
			resource.setWritten(0);

			service.saveOrUpdate(resource);
			solrIndexingListener.indexObject(resource);
		}
		
	}

}
