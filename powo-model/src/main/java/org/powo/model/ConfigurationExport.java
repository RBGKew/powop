package org.powo.model;

import java.util.List;

import org.powo.model.registry.Organisation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class ConfigurationExport {
	List<Organisation> organisations;
	List<JobConfiguration> jobConfigurations;
	List<JobList> jobLists;
}
