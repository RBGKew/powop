package org.emonocot.model.taxon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.model.common.Base;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;

public class Taxon extends Base {
	
	private List<Image> images = new ArrayList<Image>();

	public List<Image> getImages() {
		return images;
	}

	public Set<Reference> getReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Feature,Content> getContent() {
		// TODO Auto-generated method stub
		return null;
	}
}
