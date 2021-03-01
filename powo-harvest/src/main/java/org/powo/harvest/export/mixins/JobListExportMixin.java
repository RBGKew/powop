package org.powo.harvest.export.mixins;

import java.util.List;

import org.joda.time.DateTime;
import org.powo.model.JobConfiguration;
import org.powo.model.constants.JobListStatus;
import org.powo.model.marshall.json.JobListConfigurationSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public interface JobListExportMixin {

	@JsonIgnore
	public Long getId();

	@JsonIgnore
	public boolean hasNextJob();

	@JsonIgnore
	public int getCurrentJob();

	@JsonIgnore
	public DateTime getLastCompletion();

	@JsonIgnore
	public DateTime getLastAttempt();

	@JsonIgnore
	public DateTime getNextRun();

	@JsonIgnore
	public JobListStatus getStatus();

	@JsonSerialize(using = JobListConfigurationSerializer.class)
	public List<JobConfiguration> getJobConfigurations();
}
