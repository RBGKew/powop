package org.powo.portal.service;

import com.google.common.base.Strings;

import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.portal.view.helpers.NameHelper;
import org.springframework.stereotype.Component;

@Component
public class ImageCaptionService {
	public String getFullCaption(Image image, Taxon taxonBeingViewed) {
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

		// this behaviour is DIFFERENT based on the taxon being viewed! this is an extra piece of data
		// that wasn't present before
		if (taxonBeingViewed != null && image.getTaxon() != null && !taxonBeingViewed.equals(image.getTaxon())) {
			// todo: can we not have this dependency on NameHelper here? Maybe needs to be injected at least
			// will it work with passing null to options?
			var nh = new NameHelper();
			caption.append(nh.taxonLinkWithoutAuthor(image.getTaxon(), null));
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
