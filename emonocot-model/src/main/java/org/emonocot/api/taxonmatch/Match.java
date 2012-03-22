/**
 * 
 */
package org.emonocot.api.taxonmatch;

/**
 * @author jk00kg
 *
 */
public class Match {

    /**
     *
     */
    private TaxonDTO internal;

    /**
     *
     */
    private MatchStatus status;

    /**
     * @return the providers taxon
     */
    public final TaxonDTO getInternal() {
        return internal;
    }

    /**
     * @param newInternal the internal taxon to set
     */
    public final void setInternal(TaxonDTO newInternal) {
        this.internal = newInternal;
    }

    /**
     * @return the status
     */
    public final MatchStatus getStatus() {
        return status;
    }

    /**
     * @param newStatus the status to set
     */
    public final void setStatus(MatchStatus newStatus) {
        this.status = newStatus;
    }

}
