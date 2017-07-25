package org.emonocot.portal.view.helpers;

import org.emonocot.model.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Strings;

public class ImageHelper {

	@Value("${portal.cdn.key}")
	private String CDNKey;

	@Value("${portal.cdn.prefix}")
	private String CDNPrefix;

	public ImageHelper() {}

	public ImageHelper(String cdnKey, String cdnPrefix) {
		this.CDNKey = cdnKey;
		this.CDNPrefix = cdnPrefix;
	}

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
		String result = null;
		if(image.getIdentifier().startsWith("urn:kew.org:dam:")) {
			if(type.equals("thumbnail")) {
				result = cdnAsset(image, 400);
			} else {
				result = cdnAsset(image, 1600);
			}
		} else {
			result =  String.format("%s_%s.jpg", image.getAccessUri(), type);
		}

		return result;
	}

	private String cdnAsset(Image image, int size) {
		int id = Integer.parseInt(image.getIdentifier().substring(
				image.getIdentifier().lastIndexOf(':') + 1,
				image.getIdentifier().length()));

		return String.format("%s/%s.jpg",
				CDNPrefix,
				DigestUtils.md5DigestAsHex((id + "-" + size + "-" + CDNKey).getBytes()));
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
					imageUrl(image, "fullsize"), generateCaption(image), imgTag);
		}

		return new Handlebars.SafeString(imgTag);
	}

	private String generateCaption(Image image) {
		StringBuffer caption = new StringBuffer();

		caption.append(Strings.nullToEmpty(image.getTitle()));

		if(!Strings.isNullOrEmpty(image.getCaption())) {
			caption.append(" - ");
		}
		caption.append(Strings.nullToEmpty(image.getCaption()));

		caption.append("<small>");
		String owner = image.getOwner();
		String creator = image.getCreator();
		String source = image.getSource();

		if(!Strings.isNullOrEmpty(owner) && Strings.isNullOrEmpty(creator)) {
			caption.append(owner);
		} else if(!Strings.isNullOrEmpty(creator) && Strings.isNullOrEmpty(owner)) {
			caption.append(creator);
		} else if(!Strings.isNullOrEmpty(creator) && !Strings.isNullOrEmpty(owner)) {
			caption.append(creator);
		} else {
			caption.append(Strings.nullToEmpty(source));
		}
		caption.append("</small>");

		return caption.toString();
	}
}
