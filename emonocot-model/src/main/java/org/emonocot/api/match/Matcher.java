/**
 * 
 */
package org.emonocot.api.match;

import java.util.List;

import org.emonocot.api.match.Match;

/**
 * @author jk00kg
 */
public interface Matcher<I, O> {

    /**
     * For a given type input (I), return a list of matches to a given output (O)
     * @param input - The input for the matching service. This could be a string, identifier or example object 
     * @return - A list of matches, typed for convenience
     */
    List<Match<O>> getMatches(I input);

}
