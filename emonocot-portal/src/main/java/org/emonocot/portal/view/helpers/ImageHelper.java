package org.emonocot.portal.view.helpers;

import org.emonocot.model.Image;
import org.emonocot.model.registry.Organisation;

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
		String imgTag = String.format("<img src=\"%s\" title=\"%s\"/>",
				imgUrl,
				image.getTitle() == null ? "" : image.getTitle());

		StringBuffer caption = new StringBuffer();

		if(image.getTitle() != null) {
			caption.append(image.getTitle());
		}

		if(image.getCaption() != null) {
			if(image.getTitle() != null) {
				caption.append(" ");
			}
			caption.append(image.getCaption());
		}

		caption.append("<small>");
		caption.append(" ");
		if(image.getOwner() != null) {
			caption.append(image.getOwner());
		} else if(image.getCreator() != null) {
			caption.append(image.getCreator());
		}
		caption.append("</small>");

		if(figureClass != null) {
			imgTag = String.format("<figure class=\"%s\">%s</figure>", figureClass, imgTag);
		}

		if(modal) {
			imgTag = String.format("<a href=\"%s\" title=\"%s\">%s</a>",
					String.format("%s_fullsize.jpg", image.getAccessUri()), caption.toString(), imgTag);
		}

		return new Handlebars.SafeString(imgTag);
	}
}
