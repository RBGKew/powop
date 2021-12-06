package org.powo.portal.view.helpers;

import org.powo.model.Image;
import org.powo.model.helpers.CDNImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageHelper {

	private CDNImageHelper cdn;

	@Autowired
	public ImageHelper(CDNImageHelper cdn) {
		this.cdn = cdn;
	}

	public CharSequence imageToFullsizeUrl(Image image) {
		return cdn.getFullsizeUrl(image);
	}

	public CharSequence imageToThumbnailUrl(Image image) {
		return cdn.getThumbnailUrl(image);
	}
}