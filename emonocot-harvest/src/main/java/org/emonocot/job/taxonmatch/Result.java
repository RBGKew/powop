package org.emonocot.job.taxonmatch;

import java.util.Collection;
import java.util.List;

import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public class Result {
    /**
     *
     */
    private String originalIdentifier;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private TaxonMatchStatus status;

    /**
     *
     */
    private String identifier;

    /**
     * @return the originalIdentifier
     */
    public final String getOriginalIdentifier() {
        return originalIdentifier;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the status
     */
    public final TaxonMatchStatus getStatus() {
        return status;
    }

    /**
     * @return the identifier
     */
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * @return the partialMatches
     */
    public final Collection<Taxon> getPartialMatches() {
        return partialMatches;
    }

    /**
     *
     */
    private Collection<Taxon> partialMatches;

    /**
     *
     * @param newIdentifier Set the original identifier
     */
    public final void setOriginalIdentifier(final String newIdentifier) {
        this.originalIdentifier = newIdentifier;
    }

    /**
     *
     * @param newName Set the name
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

    /**
     *
     * @param newStatus Set the status
     */
    public final void setStatus(final TaxonMatchStatus newStatus) {
        this.status = newStatus;
    }

    /**
     *
     * @param newIdentifier Set the matching identifier
     */
    public final void setIdentifier(final String newIdentifier) {
        this.identifier = newIdentifier;
    }

    /**
     *
     * @param newPartialMatches Set the partial matched records
     */
    public final void setPartialMatches(
            final Collection<org.emonocot.model.taxon.Taxon> newPartialMatches) {
        this.partialMatches = newPartialMatches;
    }

}
