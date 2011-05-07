package org.emonocot.service;

import org.emonocot.model.taxon.Taxon;


/**
 *
 * @author ben
 *
 */
public interface TaxonService {

    /**
     * Verify that there is a taxon with the provided
     * identifier which has that scientific name.
     *
     * @param identifer The identifier of the taxon being verified.
     * @param scientificName The scientific name of the taxon being verified
     * @return True, if a matching taxon exists, or false if not.
     */
    boolean verify(String identifer, String scientificName);

    /**
    *
    * @param identifer
    *            The identifier of the object
    * @return the taxon, or throw a HibernateObjectNotFoundException if the
    *         object is not found.
    */
   Taxon load(String identifer);
}
