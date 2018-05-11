package org.powo.model.marshall.json;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.powo.model.JobConfiguration;
import org.powo.model.constants.JobType;
import org.powo.model.registry.Resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceWithJob {

	@NotNull
	private Resource resource;

	@NotNull
	private JobType jobType;

	private Map<String, String> params;

	private JobConfiguration jobConfiguration;

}
