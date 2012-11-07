package org.emonocot.harvest.common;

import org.emonocot.model.Taxon;

/**
 *
 * @author ben
 *
 */
public interface TaxonRelationshipResolver {

    /**
     *
     * @param taxon
     *            The taxon itself
     */
    void bind(final Taxon taxon);

    /**
     *
     * @param taxonRelationship
     *            Add a taxon relationship
     * @param identifier
     *            Set the identifier of the 'to' taxon
     */
    void addTaxonRelationship(
            final TaxonRelationship taxonRelationship, final String identifier);

    /**
     *
     * @param taxon Update the taxon to bind to
     */
    void updateTaxon(final Taxon taxon);
}
