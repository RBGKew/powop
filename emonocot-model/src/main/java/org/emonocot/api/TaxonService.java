package org.emonocot.api;

import org.emonocot.model.taxon.Taxon;


/**
 *
 * @author ben
 *
 */
public interface TaxonService extends Service<Taxon> {

    /**
     * Verify that there is a taxon with the provided
     * identifier which has that scientific name.
     *
     * @param identifer The identifier of the taxon being verified.
     * @param scientificName The scientific name of the taxon being verified
     * @return True, if a matching taxon exists, or false if not.
     */
    boolean verify(String identifer, String scientificName);
}
