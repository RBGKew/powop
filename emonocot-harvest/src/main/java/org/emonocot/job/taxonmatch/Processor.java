package org.emonocot.job.taxonmatch;

import java.util.List;

import org.emonocot.api.match.Match;
import org.emonocot.api.match.MatchStatus;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.model.Taxon;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.UnparsableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author ben
 */
public class Processor implements ItemProcessor<Taxon, Result> {

	/**
     *
     */
	private Logger logger = LoggerFactory.getLogger(Processor.class);

	/**
     *
     */
	private TaxonMatcher taxonMatcher;

	/**
	 * @param newTaxonMatcher
	 *            the matcher to set
	 */
	public void setTaxonMatcher(TaxonMatcher newTaxonMatcher) {
		this.taxonMatcher = newTaxonMatcher;
	}

	/**
	 * @param taxon
	 *            Set the taxon
	 * @return a result
	 * @throws Exception
	 *             if there is a problem
	 */
	public final Result process(final Taxon taxon) {
		logger.debug("Attempting to match " + taxon.getScientificName());
		Result result = new Result();
		Taxon internal = new Taxon();
		result.setExternal(taxon);
		result.setInternal(internal);
		result.setName(taxon.getScientificName());

		List<Match<Taxon>> matches;
		try {
			matches = taxonMatcher.match(taxon.getScientificName());

			switch (matches.size()) {
			case 0:
				logger.debug("No matches found for "
						+ taxon.getScientificName());
				result.setStatus(TaxonMatchStatus.NO_MATCH);
				result.setMatchCount(0);
				break;
			case 1:
				logger.debug("A single match identified for "
						+ taxon.getScientificName());
				Match<Taxon> single = matches.get(0);
				if (single.getStatus().equals(MatchStatus.EXACT)) {
					result.setStatus(TaxonMatchStatus.SINGLE_MATCH);
				} else {
					result.setStatus(TaxonMatchStatus.NO_EXACT_MATCH);
				}
				result.setInternal(single.getInternal());
				result.setMatchCount(1);
				break;
			default:
				logger.debug(matches.size() + " matches for "
						+ taxon.getScientificName());
				result.setMatchCount(matches.size());
				int exact = 0;
				for (Match<Taxon> match : matches) {
					if (match.getStatus().equals(MatchStatus.EXACT)) {
						exact++;
						result.setInternal(match.getInternal());
					}
				}
				if (exact == 1) {
					result.setStatus(TaxonMatchStatus.SINGLE_MATCH);
				} else if (exact > 1) {
					result.setStatus(TaxonMatchStatus.MULTIPLE_MATCHES);
					result.setInternal(internal);
				} else {
					result.setStatus(TaxonMatchStatus.NO_EXACT_MATCH);
				}
				break;
			}
		} catch (UnparsableException e) {
			result.setStatus(TaxonMatchStatus.CANNOT_PARSE);
		}
		return result;
	}

}
