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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.compare.ReferenceComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Streams;

public class Bibliography {

	private Map<String, SortedSet<Reference>> liturature;
	private SortedSet<Reference> accepted;
	private SortedSet<Reference> notAccepted;
	private Stream<Reference> referenceStream;

	private static Logger logger = LoggerFactory.getLogger(Bibliography.class);

	public Bibliography(Taxon taxon) {
		if(taxon.looksAccepted()) {
			referenceStream = Streams.concat(
					taxon.getReferences().stream(),
					taxon.getDescriptions().stream().flatMap(d -> d.getReferences().stream()),
					taxon.getDistribution().stream().flatMap(d -> d.getReferences().stream()));
		} else {
			referenceStream = taxon.getReferences().stream();
		}

		liturature = referenceStream.collect(groupingBy(r -> classify(r),
				toCollection(() -> new TreeSet<>(new ReferenceComparator()))));

		accepted = liturature.remove("Accepted");
		notAccepted = liturature.remove("Not accepted");
		if(liturature.isEmpty()) {
			liturature = null;
		}

		logger.debug("Constructed Bibliography:{}", this.toString());
	}

	private String classify(Reference reference) {
		String subject = Strings.nullToEmpty(reference.getSubject());
		String classification;

		if(!Strings.isNullOrEmpty(subject)) {
			if(subject.contains("Accepted") || subject.contains("Artificial Hybrid")) {
				classification = "Accepted";
			} else {
				classification = "Not accepted";
			}
		} else if (reference.getAuthority() != null && reference.getAuthority().getTitle() != null){
			classification = reference.getAuthority().getTitle();
		} else {
			classification = "Literature";
		}

		logger.debug("Classifying: {} as [{}]", reference, classification);
		return classification;
	}

	public Map<String, SortedSet<Reference>> getLiturature() {
		return liturature;
	}

	public Set<Reference> getAccepted() {
		return accepted;
	}

	public Set<Reference> getNotAccepted() {
		return notAccepted;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(accepted != null) {
			sb.append("\nAccepted: ");
			sb.append(accepted.toString());
		}

		if(notAccepted != null) {
			sb.append("\nSynonomized: ");
			sb.append(notAccepted.toString());
		}

		if(liturature != null) {
			sb.append("\nOthers: ");
			sb.append(Joiner.on(", ").withKeyValueSeparator("=").join(liturature));
		}

		return sb.toString();
	}
}
