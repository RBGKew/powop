package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.registry.Organisation;

public class Images {

	Taxon taxon;
	List<Image> images;
	Set<Organisation> sources;

	public Images(Taxon taxon) {
		this.taxon = taxon;
		if(taxon.isAccepted()) {
			images = taxon.getImages();
			for(Taxon synonym : taxon.getSynonymNameUsages()) {
				images.addAll(synonym.getImages());
			}
		} else {
			images = new ArrayList<>();
		}
	}

	public List<Image> getHeaderImages() {
		return images.subList(0, Math.min(images.size(), 1));
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
