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

	public CharSequence fullsizeImage(Image image, Taxon taxon, Options options) {
		return link(image, taxon, "fullsize", options);
	}

	public CharSequence thumbnailImage(Image image, Taxon taxon, Options options) {
		return link(image, taxon, "thumbnail", options);
	}

	public CharSequence imageToFullsizeUrl(Image image) {
		return cdn.getFullsizeUrl(image);
	}

	public CharSequence imageToThumbnailUrl(Image image) {
		return cdn.getThumbnailUrl(image);
	}

	public String imageUrl(Image image, String type) {
		if(type.equals("thumbnail")) {
			return cdn.getThumbnailUrl(image);
		} else {
			return cdn.getFullsizeUrl(image);
		}
	}

	private CharSequence link(Image image, Taxon taxon, String type, Options options) {
		boolean modal = options.hash("lightbox", true);
		String figureClass = options.hash("figure-class");
		String imgTag = String.format("<img src=\"%s\" alt=\"%s\"/>",
				imageUrl(image, type),
				image.getTitle() == null ? "" : image.getTitle().replace('"', '\''));

		if(figureClass != null) {
			imgTag = String.format("<figure class=\"%s\">%s</figure>", figureClass, imgTag);
		}

		if(modal) {
			imgTag = String.format("<a href=\"%s\" title=\"%s\">%s</a>",
					imageUrl(image, "fullsize"),
					image.getTitle() == null ? "" : image.getTitle().replace('"', '\''),
					imgTag);
		}

		return new Handlebars.SafeString(imgTag);
	}

	private String generateCaption(Image image, Taxon taxon, Options options) {
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

		if (taxon != null && image.getTaxon() != null && !taxon.equals(image.getTaxon())) {
			NameHelper nh = new NameHelper();
			caption.append(nh.taxonLinkWithoutAuthor(image.getTaxon(), options));
			caption.append(" | ");
		}

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