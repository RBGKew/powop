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
package org.powo.job.dwc.distribution;

import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.gbif.ecat.voc.ThreatStatus;
import org.powo.api.job.TermFactory;
import org.powo.job.dwc.read.OwnedEntityFieldSetMapper;
import org.powo.model.Distribution;
import org.powo.model.Reference;
import org.powo.model.constants.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

public class FieldSetMapper extends OwnedEntityFieldSetMapper<Distribution> {

	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	public FieldSetMapper() {
		super(Distribution.class);
	}

	@Override
	public final void mapField(final Distribution object,
			final String fieldName, final String value) throws BindException {
		super.mapField(object, fieldName, value);

		Term term = TermFactory.findTerm(fieldName);
		logger.debug("Mapping " + fieldName + " " + " " + value + " to " + object);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case identifier:
				object.setIdentifier(value);
				break;
			case source:
				if (value.indexOf(",") != -1) {
					String[] values = value.split(",");
					for (String v : values) {
						addReference(object, v);
					}
				} else {
					addReference(object, value);
				}
			default:
				break;
			}
		}

		// DwcTerms
		if (term instanceof DwcTerm) {
			DwcTerm dwcTerm = (DwcTerm) term;
			switch (dwcTerm) {
			case establishmentMeans:
				object.setEstablishmentMeans(conversionService.convert(value, EstablishmentMeans.class));
				break;
			case locality:
				object.setLocality(value);
				break;
			case locationID:
				object.setLocation(conversionService.convert(value, Location.class));
				break;
			case occurrenceRemarks:
				object.setOccurrenceRemarks(value);
				break;
			case occurrenceStatus:
				object.setOccurrenceStatus(conversionService.convert(value, OccurrenceStatus.class));
				break;
			default:
				break;
			}
		}

		if (term instanceof IucnTerm) {
			IucnTerm iucnTerm = (IucnTerm) term;
			switch (iucnTerm) {
			case threatStatus:
				object.setThreatStatus(conversionService.convert(value, ThreatStatus.class));
			}
		}
	}

	private void addReference(Distribution object, String value) {
		Reference reference = new Reference();
		reference.setIdentifier(value);
		object.getReferences().add(reference);
	}
}
