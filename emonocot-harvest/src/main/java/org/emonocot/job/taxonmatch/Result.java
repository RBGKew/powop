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
package org.emonocot.job.taxonmatch;

import java.util.Collection;

import org.emonocot.model.Taxon;

/**
 *
 * @author ben
 *
 */
public class Result {
	/**
	 *
	 */
	private Taxon originalTaxon;

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
	private int matchCount;

	/**
	 *
	 */
	private Taxon internalTaxon;

	/**
	 *
	 */
	private Collection<Taxon> partialMatches;

	/**
	 * @return the originalIdentifier
	 */
	public final Taxon getExternal() {
		return originalTaxon;
	}

	/**
	 *
	 * @param newTaxonDTO Set the original internalTaxonDto
	 */
	public final void setExternal(final Taxon newTaxonDto) {
		this.originalTaxon = newTaxonDto;
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
	 * @return the matchCount
	 */
	public final int getMatchCount() {
		return matchCount;
	}

	/**
	 * @param matchCount the matchCount to set
	 */
	public final void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	/**
	 * @return the internalTaxonDto
	 */
	public final Taxon getInternal() {
		return internalTaxon;
	}

	/**
	 *
	 * @param newTaxonDto Set the matching internalTaxonDto
	 */
	public final void setInternal(final Taxon newInternalTaxon) {
		this.internalTaxon = newInternalTaxon;
	}

	/**
	 * @return the partialMatches
	 */
	public final Collection<Taxon> getPartialMatches() {
		return partialMatches;
	}

	/**
	 *
	 * @param newPartialMatches Set the partial matched records
	 */
	public final void setPartialMatches(
			final Collection<Taxon> newPartialMatches) {
		this.partialMatches = newPartialMatches;
	}

}
