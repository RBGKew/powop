package org.powo.portal.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.powo.model.registry.Organisation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaxonImageSet {
	private List<TaxonImage> images = new ArrayList<>();
	private Set<Organisation> sources = new HashSet<>();
	private TaxonImage headerImage;

	public int imageCount() {
		return images.size();
	}
}
