package org.powo.portal.view;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxonImage {
	private String fullsizeUrl;
	private String thumbnailUrl;
	private String caption;
}
