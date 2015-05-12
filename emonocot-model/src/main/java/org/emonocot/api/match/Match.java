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
     * 
     */
    private String notes;

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

	/**
	 * @return the notes
	 */
	public final String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public final void setNotes(String notes) {
		this.notes = notes;
	}

}
