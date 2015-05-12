/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
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
