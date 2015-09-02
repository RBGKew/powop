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
package org.emonocot.job.dwc.taxon;

import org.emonocot.model.Taxon;


/**
 *
 * @author ben
 *
 */
public class TaxonRelationship {

	private Taxon from;

	private String toIdentifier;

	private String toScientificName;

	private TaxonRelationshipType term;

	public TaxonRelationship(Taxon from, TaxonRelationshipType term, String toIdentifier, String toScientificName) {
		this.from = from;
		this.toIdentifier = toIdentifier;
		this.term = term;
		this.toScientificName = toScientificName;
	}

	public TaxonRelationshipType getTerm() {
		return term;
	}

	public Taxon getFrom() {
		return from;
	}

	public String getToIdentifier() {
		return toIdentifier;
	}

	public String getToScientificName() {
		return toScientificName;
	}
}
