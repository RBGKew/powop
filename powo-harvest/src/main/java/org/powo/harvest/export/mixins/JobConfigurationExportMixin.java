package org.powo.harvest.export.mixins;

import org.springframework.batch.core.BatchStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class JobConfigurationExportMixin {
	@JsonIgnore
	private Long id;

	@JsonIgnore
	private Long lastJobExecution;

	@JsonIgnore
	private BatchStatus jobStatus;

	@JsonIgnore
	private String jobExitCode;
}