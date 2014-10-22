package org.emonocot.api.job;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.dwc.terms.UnknownTerm;

public class TermFactory {

	private static final Pattern QUOTE_PATTERN = Pattern.compile("\"");
	private final static Set<Term> unkownTerms = new HashSet<Term>();

	public static String normaliseTerm(String term) {
		// no quotes or whitespace in term names
		term = StringUtils.strip(StringUtils.trim(QUOTE_PATTERN.matcher(term)
				.replaceAll("")));
		return term.toLowerCase();
	}

	public void clear() {
		unkownTerms.clear();
	}

	/**
	 * @param termName - A string containing a fully qualified URI for a {@link Term}
	 * @return The {@link Term} represented by the parameter 
	 */
	public static Term findTerm(String termName) {
        // normalise termName
	    String normTermName;
		if (termName == null || StringUtils.isBlank(normTermName = normaliseTerm(termName))) {
			return null;
		}
		// try known term enums first
		Term term = findTermInEnum(normTermName, DwcTerm.values(),
				new String[] {DwcTerm.PREFIX, DwcTerm.NS});
		if (term == null) {
			term = findTermInEnum(normTermName, DcTerm.values(),
					new String[] {DcTerm.PREFIX + ":", "http://purl.org/dc/elements/1.1/", "dct", "dcterm:", "dcterms:", DcTerm.NS});
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
			term = findTermInEnum(normTermName, WCSPTerm.values(),
					new String[] {WCSPTerm.PREFIX, WCSPTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, SkosTerm.values(),
					new String[] {SkosTerm.PREFIX, SkosTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, unkownTerms);
		}
		if (term == null) {
			term = UnknownTerm.build(termName);
			unkownTerms.add(term);
		}
		return term;
	}

	private static Term findTermInEnum(String termName,
			Collection<Term> vocab) {
		for (Term term : vocab) {
			if (term.qualifiedName().equalsIgnoreCase(termName)) {				
				return term;
			}
			if (term.simpleName().equalsIgnoreCase(termName)) {			
				return term;
			}
		}
		return null;
	}

	private static Term findTermInEnum(String termName, Term[] vocab,
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
