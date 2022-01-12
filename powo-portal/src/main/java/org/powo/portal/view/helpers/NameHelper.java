package org.powo.portal.view.helpers;

import java.util.Arrays;

import org.apache.commons.text.WordUtils;
import org.powo.model.Taxon;
import org.springframework.stereotype.Component;
import org.gbif.ecat.voc.Rank;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;

@Component
public class NameHelper {

	private static final String[] ranks = {
			"[infrafam.unranked]",
			"[infragen.unranked]",
			"[infrasp.unranked]",
			"[infragen.grex]",
			"c.[infragen.]",
			"infragen.grex",
			"nothosubsect.",
			"nothosubtrib.",
			"supersubtrib.",
			"nothosubgen.",
			"[infragen.]",
			"nothosubsp.",
			"subsubforma",
			"grex_sect.",
			"[infragen]",
			"nothosect.",
			"subgenitor",
			"subsubvar.",
			"supersect.",
			"supertrib.",
			"suprasect.",
			"agamovar.",
			"[epsilon]",
			"gen. ser.",
			"microgen.",
			"nothogrex",
			"nothoser.",
			"nothovar.",
			"superser.",
			"agamosp.",
			"[alpha].",
			"[gamma].",
			"subhybr.",
			"subsect.",
			"subspec.",
			"subtrib.",
			"agglom.",
			"[beta].",
			"convar.",
			"genitor",
			"monstr.",
			"nothof.",
			"subfam.",
			"subgen.",
			"sublus.",
			"subser.",
			"subvar.",
			"f.juv.",
			"Gruppe",
			"modif.",
			"proles",
			"stirps",
			"subsp.",
			"cycl.",
			"forma",
			"group",
			"linea",
			"prol.",
			"sect.",
			"spec.",
			"subf.",
			"trib.",
			"fam.",
			"gen.",
			"grex",
			"lus.",
			"mut.",
			"oec.",
			"psp.",
			"race",
			"ser.",
			"var.",
			"ap.",
			"II.",
			"nm.",
			"f."
	};

	public CharSequence taxonName(Taxon taxon) {
		if (taxon != null && taxon.getScientificName() != null) {
			String formatted = "<em lang='la'>" + taxon.getScientificName() + "</em>";
			// look for the rank part of a name string and de-italicise it
			for (String rank : ranks) {
				if (formatted.contains(rank)) {
					formatted = formatted.replace(" " + rank + " ", "</em> " + rank + " <em lang='la'>");
				}
			}

			return formatted;
		} else {
			return "";
		}
	}

	public CharSequence nameAndAuthor(final Taxon taxon, final Options options) {
		String result = String.format("%s %s", taxonName(taxon), taxon.getScientificNameAuthorship());
		return new Handlebars.SafeString(result);
	}
	
	public CharSequence taxonNameAndAuthor(final Taxon taxon, final Options options) {
		String result = String.format("%s <small>%s</small>", taxonName(taxon), taxon.getScientificNameAuthorship());
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
				taxonName(taxon));
		return new Handlebars.SafeString(result);
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
}