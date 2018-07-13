package org.powo.model;

import java.util.List;

import org.powo.model.registry.Organisation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationExport {
	List<Organisation> organisations;
	List<JobConfiguration> jobConfigurations;
	List<JobList> jobLists;
}
