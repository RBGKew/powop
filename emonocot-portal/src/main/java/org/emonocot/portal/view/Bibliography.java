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
package org.emonocot.portal.view;

import java.util.Set;
import java.util.TreeSet;

import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.compare.ReferenceComparator;

public class Bibliography {

	class BibliographyItem implements Comparable<BibliographyItem> {
		public Reference reference;
		public int key;

		public BibliographyItem(Reference reference) {
			this.reference = reference;
		}

		@Override
		public int compareTo(BibliographyItem other) {
			return new ReferenceComparator().compare(reference, other.reference);
		}
	}

	private Set<BibliographyItem> references;
	private Set<BibliographyItem> accepted;
	private Set<BibliographyItem> synonomized;

	public Bibliography(Taxon taxon) {
		references = new TreeSet<>();
		if(taxon.isAccepted()) {
			collectReferences(taxon);
			collectDescriptionReferences(taxon);
			collectDistributionReferences(taxon);
			for(Taxon synonym : taxon.getSynonymNameUsages()) {
				collectDescriptionReferences(synonym);
			}
		} else {
			collectReferences(taxon);
		}

		int code = 1;
		for(BibliographyItem item : references) {
			item.key = code++;
		}

		references.removeAll(getAcceptedIn());
		references.removeAll(getSynonomizedIn());
	}

	private void collectReferences(Taxon taxon) {
		for(Reference reference : taxon.getReferences()) {
			references.add(new BibliographyItem(reference));
		}
	}

	private void collectDescriptionReferences(Taxon taxon) {
		for (Description d : taxon.getDescriptions()) {
			for(Reference reference : d.getReferences()) {
				references.add(new BibliographyItem(reference));
			}
		}
	}

	private void collectDistributionReferences(Taxon taxon) {
		for (Distribution d : taxon.getDistribution()) {
			for(Reference reference : d.getReferences()) {
				references.add(new BibliographyItem(reference));
			}
		}
	}

	private Set<BibliographyItem> filterOnSubject(String subject) {
		Set<BibliographyItem> filtered = new TreeSet<>();
		for(BibliographyItem item : references) {
			String rs = item.reference.getSubject();
			if(rs != null && rs.contains(subject)) {
				filtered.add(item);
			}
		}
		return filtered;
	}

	public Set<BibliographyItem> getAcceptedIn() {
		if(accepted == null) {
			accepted = filterOnSubject("Accepted");
		}

		return accepted;
	}

	public Set<BibliographyItem> getSynonomizedIn() {
		if(synonomized == null) {
			synonomized = filterOnSubject("Synonym");
		}

		return synonomized;
	}

	public Set<BibliographyItem> getReferences() {
		return this.references;
	}
}
