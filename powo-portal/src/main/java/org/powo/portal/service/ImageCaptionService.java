package org.powo.portal.service;

import com.google.common.base.Strings;

import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.portal.view.helpers.NameHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageCaptionService {
	private final NameHelper nameHelper;

	public String getHtmlCaption(Image image, Taxon taxonBeingViewed) {
		var caption = new StringBuffer();

		caption.append(Strings.nullToEmpty(image.getTitle()));

		if (!Strings.isNullOrEmpty(image.getCaption())) {
			caption.append(" - ");
		}
		caption.append(Strings.nullToEmpty(image.getCaption()));

		caption.append("<small>");
		var owner = image.getOwner();
		var creator = image.getCreator();
		var source = image.getSource();

		// this behaviour is DIFFERENT based on the taxon being viewed!
		// e.g. if an image is being shown on the species page it belongs to we don't show the taxon link
		// BUT if we are showing an image on a genus page it will give the link to the species it's from
		if (taxonBeingViewed != null && image.getTaxon() != null && !taxonBeingViewed.equals(image.getTaxon())) {
			caption.append(nameHelper.taxonLinkWithoutAuthor(image.getTaxon(), null));
			caption.append(" | ");
		}

		if (!Strings.isNullOrEmpty(owner) && Strings.isNullOrEmpty(creator)) {
			caption.append(owner);
		} else if (!Strings.isNullOrEmpty(creator) && Strings.isNullOrEmpty(owner)) {
			caption.append(creator);
		} else if (!Strings.isNullOrEmpty(creator) && !Strings.isNullOrEmpty(owner)) {
			caption.append(creator);
		} else {
			caption.append(Strings.nullToEmpty(source));
		}
		caption.append("</small>");

		return caption.toString();
	}
}
