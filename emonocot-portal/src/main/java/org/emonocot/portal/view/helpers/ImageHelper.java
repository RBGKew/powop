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
		boolean modal = options.hash("modal", true);
		String modalOptions = "data-target=\".gallery-modal-fullscreen\" data-toggle=\"modal\"";
		String imgTag = String.format("<img src=\"%s_%s.jpg\" alt=\"%s: %s\" %s/>",
				image.getAccessUri(),
				type,
				image.getTitle(),
				image.getCaption(),
				modal ? modalOptions : "");

		return new Handlebars.SafeString(imgTag);
	}
}
