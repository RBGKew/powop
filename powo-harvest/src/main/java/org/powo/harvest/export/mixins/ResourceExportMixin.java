package org.powo.harvest.export.mixins;

import org.powo.model.JobConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResourceExportMixin {

	@JsonIgnore
	private Long id;

	@JsonIgnore
	private JobConfiguration jobConfiguration;

}
