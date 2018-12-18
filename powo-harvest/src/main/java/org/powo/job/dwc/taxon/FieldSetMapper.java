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
package org.powo.job.dwc.taxon;

import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.powo.model.constants.TaxonomicStatus;
import org.powo.api.job.EmonocotTerm;
import org.powo.api.job.TermFactory;
import org.powo.job.dwc.read.BaseDataFieldSetMapper;
import org.powo.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionException;
import org.springframework.validation.BindException;

public class FieldSetMapper extends BaseDataFieldSetMapper<Taxon> {

	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	public FieldSetMapper() {
		super(Taxon.class);
	}

	/**
	 *
	 * @param object the object to map onto
	 * @param fieldName the name of the field
	 * @param value the value to map
	 * @throws BindException if there is a problem mapping
	 *         the value to the object
	 */
	@Override
	public final void mapField(final Taxon object, final String fieldName, final String value) throws BindException {
		super.mapField(object, fieldName, value);

		Term term = TermFactory.findTerm(fieldName);
		logger.debug("Mapping " + fieldName + " " + " " + value + " to "
				+ object);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case bibliographicCitation:
				object.setBibliographicCitation(value);
				break;
			case references:
				object.setUri(value);
				break;
			case source:
				object.setSource(value);
				break;
			default:
				break;
			}
		}

		// DwcTerms
		if (term instanceof DwcTerm) {
			DwcTerm dwcTerm = (DwcTerm) term;
			switch (dwcTerm) {
			case class_:
				object.setClazz(value);
				break;
			case family:
				object.setFamily(value);
				break;
			case genus:
				object.setGenus(value);
				break;
			case infraspecificEpithet:
				object.setInfraspecificEpithet(value);
				break;
			case kingdom:
				object.setKingdom(value);
				break;
			case nameAccordingToID:
				// not mapped
				break;
			case namePublishedInID:
				// not mapped
				break;
			case namePublishedIn:
				object.setNamePublishedInString(value);
				break;
			case namePublishedInYear:
				object.setNamePublishedInYear(conversionService.convert(value, Integer.class));
				break;
			case nomenclaturalCode:
				object.setNomenclaturalCode(conversionService.convert(value, NomenclaturalCode.class));
				break;
			case nomenclaturalStatus:
				object.setNomenclaturalStatus(conversionService.convert(value, NomenclaturalStatus.class));
				break;
			case order:
				object.setOrder(value);
				break;
			case phylum:
				object.setPhylum(value);
				break;
			case scientificName:
				object.setScientificName(value);
				break;
			case scientificNameAuthorship:
				object.setScientificNameAuthorship(value);
				break;
			case scientificNameID:
				object.setScientificNameID(value);
				break;
			case specificEpithet:
				object.setSpecificEpithet(value);
				break;
			case subgenus:
				object.setSubgenus(value);
				break;
			case taxonID:
				object.setIdentifier(value);
				break;
			case taxonomicStatus:
				try {
					object.setTaxonomicStatus(conversionService.convert(value, TaxonomicStatus.class));
				} catch (ConversionException ce) {
					logger.error(ce.getMessage());
				}
				break;
			case taxonRank:
				try {
					object.setTaxonRank(conversionService.convert(value, Rank.class));
				} catch (ConversionException ce) {
					logger.error(ce.getMessage());
				}
				break;
			case taxonRemarks:
				object.setTaxonRemarks(value);
				break;
			case verbatimTaxonRank:
				object.setVerbatimTaxonRank(value);
				break;
			default:
				break;
			}
		}
		// eMonocot Terms
		if (term instanceof EmonocotTerm) {
			EmonocotTerm eMonocotTerm = (EmonocotTerm) term;
			switch(eMonocotTerm) {
			case subfamily:
				object.setSubfamily(value);
				break;
			case subtribe:
				object.setSubtribe(value);
				break;
			case tribe:
				object.setTribe(value);
				break;
			default:
				break;
			}
		}
	}
}
