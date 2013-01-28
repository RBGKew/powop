package org.emonocot.api.job;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.dwc.terms.UnknownTerm;

public class TermFactory {

	private static final Pattern QUOTE_PATTERN = Pattern.compile("\"");
	private final Set<ConceptTerm> unkownTerms = new HashSet<ConceptTerm>();

	public static String normaliseTerm(String term) {
		// no quotes or whitespace in term names
		term = StringUtils.strip(StringUtils.trim(QUOTE_PATTERN.matcher(term)
				.replaceAll("")));
		return term.toLowerCase();
	}

	public void clear() {
		unkownTerms.clear();
	}

	public ConceptTerm findTerm(String termName) {
		if (termName == null) {
			return null;
		}
		// normalise terms
		String normTermName = normaliseTerm(termName);
		// try known term enums first
		ConceptTerm term = findTermInEnum(normTermName, DwcTerm.values(),
				new String[] {DwcTerm.PREFIX, DwcTerm.NS});
		if (term == null) {
			term = findTermInEnum(normTermName, DcTerm.values(),
					new String[] {DcTerm.PREFIX, DcTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, GbifTerm.values(),
					new String[] {GbifTerm.PREFIX, GbifTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, IucnTerm.values(),
					new String[] {IucnTerm.PREFIX, IucnTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, Wgs84Term.values(),
					new String[] {Wgs84Term.PREFIX, Wgs84Term.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, EmonocotTerm.values(),
					new String[] {EmonocotTerm.PREFIX, EmonocotTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, unkownTerms);
		}
		if (term == null) {
			term = new UnknownTerm(termName, StringUtils.substringAfterLast(
					termName, "/"));
			unkownTerms.add(term);
		}
		return term;
	}

	private ConceptTerm findTermInEnum(String termName,
			Collection<ConceptTerm> vocab) {
		for (ConceptTerm term : vocab) {
			if (term.qualifiedNormalisedName().equalsIgnoreCase(termName)) {
				return term;
			}
			if (term.simpleNormalisedName().equalsIgnoreCase(termName)) {
				return term;
			}
			for (String alt : term.simpleNormalisedAlternativeNames()) {
				if (alt.equalsIgnoreCase(termName)) {
					return term;
				}
			}
		}
		return null;
	}

	private ConceptTerm findTermInEnum(String termName, ConceptTerm[] vocab,
			String[] prefixes) {
		for (String prefix : prefixes) {
			if (termName.startsWith(prefix)) {
				termName = termName.substring(prefix.length());
				break;
			}
		}
		return findTermInEnum(termName, Arrays.asList(vocab));
	}

}
