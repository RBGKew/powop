/**
 * 
 */
package org.emonocot.api.match;

/**
 * @author jk00kg
 *
 */
public class Match<T> {

    /**
     *
     */
    private T internal;
    
    /**
     * 
     */
    private float similarity = 0.0f;

    /**
     *
     */
    private MatchStatus status;

    /**
     * @return the providers taxon
     */
    public final T getInternal() {
        return internal;
    }

    /**
     * @param newInternal the internal taxon to set
     */
    public final void setInternal(T newInternal) {
        this.internal = newInternal;
    }

    /**
     * @param similarity the similarity to set
     */
    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    /**
     * @return the similarity
     */
    public float getSimilarity() {
        if(status == null) {
            return similarity;
        } else if (status == MatchStatus.EXACT){
            return 1.0f;
        } else {
            return 0.0f;
        }
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
