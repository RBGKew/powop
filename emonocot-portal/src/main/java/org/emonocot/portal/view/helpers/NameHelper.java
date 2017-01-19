package org.emonocot.portal.view.helpers;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.emonocot.model.Taxon;
import org.gbif.ecat.voc.Rank;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import com.google.common.base.Strings;

public class NameHelper {

	public CharSequence nameAndAuthor(final Taxon taxon, final Options options) {
		String result = String.format("<em>%s</em> %s", taxon.getScientificName(), taxon.getScientificNameAuthorship());
		return new Handlebars.SafeString(result);
	}
	
	public CharSequence taxonNameAndAuthor(final Taxon taxon, final Options options) {
		String result = String.format("%s <small>%s</small>", taxon.getScientificName(), taxon.getScientificNameAuthorship());
		return new Handlebars.SafeString(result);
	}

	public CharSequence taxonLink(final Taxon taxon, final Options options) {
		String result = String.format("<a href=\"/taxon/%s\">%s</a>",
				taxon.getIdentifier(),
				options.param(0, nameAndAuthor(taxon, options)));
		return new Handlebars.SafeString(result);
	}

	public CharSequence taxonLinkWithoutAuthor(final Taxon taxon, final Options options) {
		String result = String.format("<a href=\"/taxon/%s\">%s</a>",
				taxon.getIdentifier(),
				taxon.getScientificName());
		return new Handlebars.SafeString(result);
	}

	public CharSequence classification(Taxon taxon, Options options) {
		List<Taxon> higherClassification = taxon.getHigherClassification();
		String classification = classificationLine(higherClassification, 0, options);
		return new Handlebars.SafeString(classification);
	}

	public CharSequence childRank(Taxon taxon, Options options) {
		int index = Arrays.asList(Rank.LINNEAN_RANKS).indexOf(taxon.getTaxonRank());
		if(taxon.getTaxonRank() == Rank.FAMILY) {
			return "Genera";
		} else if(index == -1) {
			return "Unknown Rank";
		} else if(index == Rank.LINNEAN_RANKS.length - 1) {
			return "Infraspecifics";
		} else {
			return WordUtils.capitalizeFully(Rank.LINNEAN_RANKS[index+1].toString());
		}
	}

	private String classificationLine(List<Taxon> classification, int index, Options options) {
		if(index == classification.size()-1) {
			return String.format("<ol><li><h1 class=\"c-summary__heading\">%s</h1></li><ol>",
					taxonNameAndAuthor(classification.get(index), options));
		} else {
			if(classification.get(index).getTaxonRank() == null) {
				return String.format("<ol><li>%s%s</li></ol>",
						taxonLink(classification.get(index), options),
						classificationLine(classification, index+1, options));
			} else {
				return String.format("<ol><li>%s: %s%s</li></ol>",
						WordUtils.capitalize(classification.get(index).getTaxonRank().toString().toLowerCase()),
						taxonLink(classification.get(index), options),
						classificationLine(classification, index+1, options));
			}
		}
	}
}