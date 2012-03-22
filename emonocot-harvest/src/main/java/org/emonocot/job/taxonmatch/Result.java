package org.emonocot.job.taxonmatch;

import java.util.Collection;

import org.emonocot.api.taxonmatch.TaxonDTO;

/**
 *
 * @author ben
 *
 */
public class Result {
    /**
     *
     */
    private TaxonDTO originalTaxonDto;

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
    private TaxonDTO internalTaxonDto;

    /**
     *
     */
    private Collection<TaxonDTO> partialMatches;

    /**
     * @return the originalIdentifier
     */
    public final TaxonDTO getExternal() {
        return originalTaxonDto;
    }

    /**
     *
     * @param newTaxonDTO Set the original internalTaxonDto
     */
    public final void setExternal(final TaxonDTO newTaxonDto) {
        this.originalTaxonDto = newTaxonDto;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     *
     * @param newName Set the name
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

    /**
     * @return the status
     */
    public final TaxonMatchStatus getStatus() {
        return status;
    }

    /**
     *
     * @param newStatus Set the status
     */
    public final void setStatus(final TaxonMatchStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * @return the internalTaxonDto
     */
    public final TaxonDTO getInternal() {
        return internalTaxonDto;
    }

    /**
     *
     * @param newInternalTaxonDto Set the matching internalTaxonDto
     */
    public final void setInternal(final TaxonDTO newInternalTaxonDto) {
        this.internalTaxonDto = newInternalTaxonDto;
    }

    /**
     * @return the partialMatches
     */
    public final Collection<TaxonDTO> getPartialMatches() {
        return partialMatches;
    }

    /**
     *
     * @param newPartialMatches Set the partial matched records
     */
    public final void setPartialMatches(
            final Collection<TaxonDTO> newPartialMatches) {
        this.partialMatches = newPartialMatches;
    }

}
