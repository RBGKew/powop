package org.powo.harvest.export.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class OrganisationMixin {
	@JsonIgnore
	private Long id;
}
