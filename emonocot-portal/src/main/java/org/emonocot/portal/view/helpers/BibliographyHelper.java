package org.emonocot.portal.view.helpers;

import org.emonocot.model.registry.Organisation;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;

public class BibliographyHelper {

	public CharSequence bracketedSourceLink(final Organisation org, final Options options) {
		String link =  String.format("<a href=\"#source-%s\" %s>[%s]</a>", org.getAbbreviation(), tooltip(org.getTitle()), org.getAbbreviation());
		return new Handlebars.SafeString(link);
	}

	public CharSequence sourceLink(final Organisation org, final Options options) {
		String link =  String.format("<a href=\"#source-%s\" %s>%s</a>", org.getAbbreviation(), tooltip(org.getTitle()), org.getAbbreviation());
		return new Handlebars.SafeString(link);
	}

	private String tooltip(String title) {
		return String.format("data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"%s\"", title);
	}
}