package org.emonocot.portal.view;

import java.util.List;

import org.emonocot.model.Image;
import org.emonocot.model.Taxon;

public class Images {

	Taxon taxon;
	List<Image> images;

	public Images(Taxon taxon) {
		this.taxon = taxon;
		this.images = taxon.getImages();
	}

	public List<Image> getHeaderImages() {
		return images.subList(0, Math.min(images.size(), 3));
	}
	
	public List<Image> getAll() {
		return images;
	}
}
