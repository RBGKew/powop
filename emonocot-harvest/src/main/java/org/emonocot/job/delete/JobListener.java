package org.emonocot.job.delete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.emonocot.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;

public class JobListener implements JobExecutionListener {
	
	private static Logger logger = LoggerFactory.getLogger(JobListener.class);

	ResourceService resourceService;
	
	
	public void setResourceService(ResourceService resourceService){
		this.resourceService = resourceService;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		logger.debug(jobExecution.getStatus().toString());
		String resource_id = (String) jobExecution.getJobInstance().getJobParameters().getString("resource_id");
		Resource resource = resourceService.load(Long.parseLong(resource_id));
		if(jobExecution.getStatus().equals(BatchStatus.COMPLETED)){
			resource.setExitCode("RECORDS DELETED");
			resource.setRecordsRead(0);
			resource.setWritten(0);
			resource.setExitDescription("All associated records deleted. Press delete again to completely remove the resource, or harvest to reharvest all records.");
			resourceService.saveOrUpdate(resource);
			
		}else{
			Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
			if(stepExecutions != null && !stepExecutions.isEmpty()){
				for(StepExecution step : stepExecutions){
					if(step.getExitStatus().equals(new ExitStatus("DELETE FAILED"))){
						resource.setExitCode("DELETE FAILED");
						resource.setExitDescription(step.getExitStatus().getExitDescription());
					}
				}
			
			}
			
		}
	}
	


	@Override
	public void beforeJob(JobExecution arg0) {
		// TODO Auto-generated method stub
		
	}

}
