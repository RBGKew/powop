/**
 * 
 */
package org.emonocot.api.taxonmatch;

import java.util.List;

import org.gbif.ecat.model.ParsedName;

/**
 * @author jk00kg
 */
public interface TaxonMatcher {

    /**
     * @param parsed
     *            a gbif-ecat parsed name
     * @return a list of DTOs matching the parameter The specific criteria of a
     *         match and therefore the DTOs returned are dependent on the
     *         implementation. This allows for accepted taxa, name
     *         inclusive/exclusive of Authorship to be considered as well as
     *         partial matches to optionally be returned
     */
    List<Match> match(ParsedName<String> parsed);

}
