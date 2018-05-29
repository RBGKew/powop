package org.powo.harvest.export.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ResourceMixin {
	@JsonIgnore
	private Long id;
}
