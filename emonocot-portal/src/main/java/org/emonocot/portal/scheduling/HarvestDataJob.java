package org.emonocot.portal.scheduling;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.registry.Resource;
import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class HarvestDataJob extends QuartzJobBean {
	
	private Logger logger = LoggerFactory.getLogger(HarvestDataJob.class);
	
	private List<String> cronExpressions = new ArrayList<String>();
	
	private String jobLauncherName;
	
	private String resourceServiceName;

	public void setWorkingWeekCronExpression(String workingWeekCronExpression) {
		this.cronExpressions.add(workingWeekCronExpression);
	}
	
	public void seWeekendCronExpression(String weekendCronExpression) {
		this.cronExpressions.add(weekendCronExpression);
	}

	public void setResourceServiceName(String resourceServiceName) {
		this.resourceServiceName = resourceServiceName;
	}

	public void setJobLauncherName(String jobLauncherName) {
		this.jobLauncherName = jobLauncherName;
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {

		try {
			SchedulerContext schedulerContext = jobExecutionContext.getScheduler().getContext();
			ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get("applicationContext");
			ResourceService resourceService = (ResourceService) applicationContext.getBean(resourceServiceName);
			JobLauncher jobLauncher = (JobLauncher) applicationContext.getBean(jobLauncherName);
			logger.info("HarvestDataJob");
			for (String cronExpression : cronExpressions) {
				CronExpression expression = new CronExpression(cronExpression);
				DateTime now = new DateTime();
				
				if (expression.isSatisfiedBy(now.toDate())	&& !resourceService.isHarvesting()) {
					DateTime nextInvalidDate = new DateTime(expression.getNextInvalidTimeAfter(now.toDate()));
					logger.info(cronExpression + " is satified and resourceService is not harvesting, looking for jobs to harvest . . .");
					List<Resource> resourcesToHarvest = resourceService.listResourcesToHarvest(10, now,"job-with-source");
					Resource resource = null;
					for (Resource r : resourcesToHarvest) {
						DateTime probableFinishingTime = now.plus(r.getDuration());
						if (probableFinishingTime.isBefore(nextInvalidDate)) {
							resource = r;
							break;
						}
					}

					if (resource != null) {
						logger.error("Found that we can harvest " + resource.getTitle());
						Map<String, String> jobParametersMap = new HashMap<String, String>();
						jobParametersMap.put("authority.name", resource.getOrganisation().getIdentifier());
						jobParametersMap.put("authority.uri", resource.getUri());
						jobParametersMap.put("resource.identifier",	resource.getIdentifier());
						jobParametersMap.put("authority.last.harvested", Long.toString((resource.getStartTime().getMillis())));
						jobParametersMap.putAll(resource.getParameters());

						JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
						jobLaunchRequest.setJob(resource.getResourceType().getJobName());
						jobLaunchRequest.setParameters(jobParametersMap);

						try {
							jobLauncher.launch(jobLaunchRequest);
							resource.setStartTime(null);
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
							resourceService.saveOrUpdate(resource);
						} catch (org.emonocot.api.job.JobExecutionException jee) {
							throw new JobExecutionException(jee);
						}

					} else {
						logger.info("Could not find a resource we can safely harvest in the time available");
					}

				} else {
					logger.info(now + " is not within " + cronExpression + "or resourceService is harvesting, skipping!");
				}
			}
		} catch (ParseException pe) {
			throw new JobExecutionException(pe);
		} catch (SchedulerException se) {
			throw new JobExecutionException(se);
		}
	}
}
