package org.powo.portal.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.powo.api.ImageService;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Images {

	private static final Logger logger = LoggerFactory.getLogger(Images.class);

	Taxon taxon;
	List<Image> images;
	Set<Organisation> sources;

	public Images(Taxon taxon, ImageService imageService) {
		this.taxon = taxon;
		if(taxon.looksAccepted()) {
			images = taxon.getImages();
			for(Taxon synonym : taxon.getSynonymNameUsages()) {
				images.addAll(synonym.getImages());
			}

			if(images.size() < 6) {
				List<Image> img = imageService.getTopImages(taxon, 6 - images.size());
				images.addAll(img);
			}
		} else {
			images = new ArrayList<>();
		}
	}

	public Image getHeaderImage() {
		return images.get(0);
	}

	public List<Image> getAll() {
		return images;
	}

	public Set<Organisation> getSources() {
		if(sources == null) {
			sources = new HashSet<>();
			for(Image img : images) {
				sources.add(img.getAuthority());
			}
		}

		return sources;
	}
}
