package org.emonocot.portal.view.helpers;

import org.emonocot.model.Image;
import org.emonocot.model.registry.Organisation;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Strings;

public class ImageHelper {

	public CharSequence fullsizeImage(Image image, Options options) {
		return link(image, "fullsize", options);
	}

	public CharSequence thumbnailImage(Image image, Options options) {
		return link(image, "thumbnail", options);
	}

	public CharSequence fullsizeUrl(Image image) {
		return imageUrl(image, "fullsize");
	}

	public CharSequence thumbnailUrl(Image image) {
		return imageUrl(image, "thumbnail");
	}

	private String imageUrl(Image image, String type) {
		if(image.getIdentifier().startsWith("urn:kew.org:dam")){
			if(type.equals("thumbnail")){
				return String.format("%s%s", image.getAccessUri(), "?s=400&k=131f04e3b359a15762abfab29c7001d9");
			}else{
				return String.format("%s%s", image.getAccessUri(), "?s=400&k=131f04e3b359a15762abfab29c7001d9");
			}
		}
		return String.format("%s_%s.jpg", image.getAccessUri(), type);
	}

	private CharSequence link(Image image, String type, Options options) {
		boolean modal = options.hash("lightbox", true);
		String figureClass = options.hash("figure-class");
		String imgTag = String.format("<img src=\"%s\" title=\"%s\"/>",
				imageUrl(image, type),
				image.getTitle() == null ? "" : image.getTitle());

		if(figureClass != null) {
			imgTag = String.format("<figure class=\"%s\">%s</figure>", figureClass, imgTag);
		}

		if(modal) {
			imgTag = String.format("<a href=\"%s\" title=\"%s\">%s</a>",
					String.format("%s_fullsize.jpg", imageUrl(image, "fullsize")), generateCaption(image), imgTag);
		}

		return new Handlebars.SafeString(imgTag);
	}

	private String generateCaption(Image image) {
		StringBuffer caption = new StringBuffer();

		caption.append(Strings.nullToEmpty(image.getTitle()));

		if(!Strings.isNullOrEmpty(image.getTitle())) {
			caption.append(" ");
		}
		caption.append(Strings.nullToEmpty(image.getCaption()));

		caption.append("<small>");
		String owner = image.getOwner();
		String creator = image.getCreator();
		String source = image.getSource();

		if(!Strings.isNullOrEmpty(owner) && Strings.isNullOrEmpty(creator)) {
			caption.append("© ");
			caption.append(owner);
		} else if(!Strings.isNullOrEmpty(creator) && Strings.isNullOrEmpty(owner)) {
			caption.append("© ");
			caption.append(creator);
		} else if(!Strings.isNullOrEmpty(creator) && !Strings.isNullOrEmpty(owner)) {
			if(creator.equals(owner)) {
				caption.append(" © ");
				caption.append(owner);
			} else {
				caption.append(creator);
				caption.append(" © ");
				caption.append(owner);
			}
		} else {
			caption.append(source);
		}
		caption.append("</small>");

		return caption.toString();
	}
}
