package org.powo.model.marshall.json;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.powo.model.constants.JobType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobWithParams {

	@NotNull
	private JobType jobType;

	private Map<String, String> params;

	private String description;
}
