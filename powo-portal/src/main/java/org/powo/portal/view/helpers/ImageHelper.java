package org.powo.portal.view.helpers;

import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.helpers.CDNImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Strings;

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