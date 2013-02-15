/**
 * 
 */
package org.emonocot.api.match.taxon;

import java.util.List;

import org.emonocot.api.match.Match;
import org.emonocot.model.Taxon;
import org.gbif.ecat.parser.UnparsableException;

/**
 * @author jk00kg
 */
public interface TaxonMatcher {

    /**
     * @param parsed
     *            a gbif-ecat parsed name
     * @return a list of "Taxon"s matching the parameter The specific criteria of a
     *         match and therefore the objects returned are dependent on the
     *         implementation. This allows for accepted taxa, name
     *         inclusive/exclusive of Authorship to be considered as well as
     *         partial matches to optionally be returned
     */
    List<Match<Taxon>> match(String name) throws UnparsableException;

}
