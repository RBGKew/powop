package org.emonocot.portal.view.helpers;

import org.emonocot.model.Image;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;

public class ImageHelper {

	public CharSequence fullsizeImage(Image image, Options options) {
		return link(image, "fullsize", options);
	}

	public CharSequence thumbnailImage(Image image, Options options) {
		return link(image, "thumbnail", options);
	}

	private CharSequence link(Image image, String type, Options options) {
		boolean modal = options.hash("lightbox", true);
		String figureClass = options.hash("figure-class");
		String imgUrl = String.format("%s_%s.jpg", image.getAccessUri(), type);
		String imgTag = String.format("<img src=\"%s\" title=\"%s\"/>", imgUrl, image.getTitle());

		if(figureClass != null) {
			imgTag = String.format("<figure class=\"%s\">%s</figure>", figureClass, imgTag);
		}

		if(modal) {
			imgTag = String.format("<a href=\"%s\" data-toggle=\"lightbox\" data-gallery=\"image-gallery\" data-title=\"%s\" data-footer=\"%s\">%s</a>",
					String.format("%s_fullsize.jpg", image.getAccessUri()), image.getTitle(), image.getCaption(), imgTag);
		}

		return new Handlebars.SafeString(imgTag);
	}
}
