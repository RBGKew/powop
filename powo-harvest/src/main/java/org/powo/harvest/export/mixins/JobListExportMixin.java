package org.powo.harvest.export.mixins;

import java.util.List;

import org.powo.model.JobConfiguration;
import org.powo.model.marshall.json.JobListConfigurationSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public abstract class JobListExportMixin {

	@JsonIgnore
	private Long id;

	@JsonIgnore
	public boolean hasNextJob;

	@JsonIgnore
	public int currentJob;

	@JsonSerialize(using = JobListConfigurationSerializer.class)
	private List<JobConfiguration> jobConfigurations;
}
