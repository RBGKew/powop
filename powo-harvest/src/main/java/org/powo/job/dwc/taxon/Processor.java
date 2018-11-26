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

import org.powo.job.dwc.exception.NoIdentifierException;
import org.powo.job.dwc.read.DarwinCoreProcessor;
import org.powo.model.Taxon;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class Processor extends DarwinCoreProcessor<Taxon> implements ChunkListener, ItemWriteListener<Taxon> {

	private Logger logger = LoggerFactory.getLogger(Processor.class);

	public Taxon doProcess(Taxon t) throws Exception {
		logger.debug("Processing {}", t.getIdentifier());

		if (t.getIdentifier() == null) {
			throw new NoIdentifierException(t);
		}

		Taxon persisted = getTaxonService().find(t.getIdentifier());

		if (persisted == null) {
			// Taxon is new
			validate(t);
			chunkAnnotations.add(createAnnotation(t, RecordType.Taxon, AnnotationCode.Create, AnnotationType.Info));
			t.setAuthority(getSource());
			t.setResource(getResource());

			// don't try saving any linked taxa. This must be done by the linking processor
			t.setParentNameUsage(null);
			t.setAcceptedNameUsage(null);
			t.setOriginalNameUsage(null);

			logger.debug("Adding taxon {}", t);
			return t;
		} else {
			checkAuthority(RecordType.Taxon, t, persisted.getAuthority());
			if (skipUnmodified
					&& ((persisted.getModified() != null && t.getModified() != null) && !persisted
							.getModified().isBefore(t.getModified()))) {
				replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Skipped);
			} else {
				persisted.setAccessRights(t.getAccessRights());
				persisted.setCreated(t.getCreated());
				persisted.setLicense(t.getLicense());
				persisted.setModified(t.getModified());
				persisted.setRights(t.getRights());
				persisted.setRightsHolder(t.getRightsHolder());
				persisted.setBibliographicCitation(t.getBibliographicCitation());
				persisted.setClazz(t.getClazz());
				persisted.setFamily(t.getFamily());
				persisted.setGenus(t.getGenus());
				persisted.setInfraspecificEpithet(t.getInfraspecificEpithet());
				persisted.setKingdom(t.getKingdom());
				persisted.setNamePublishedInString(t.getNamePublishedInString());
				persisted.setNamePublishedInYear(t.getNamePublishedInYear());
				persisted.setNomenclaturalCode(t.getNomenclaturalCode());
				persisted.setNomenclaturalStatus(t.getNomenclaturalStatus());
				persisted.setOrder(t.getOrder());
				persisted.setPhylum(t.getPhylum());
				persisted.setScientificName(t.getScientificName());
				persisted.setScientificNameAuthorship(t.getScientificNameAuthorship());
				persisted.setScientificNameID(t.getScientificNameID());
				persisted.setSource(t.getSource());
				persisted.setSpecificEpithet(t.getSpecificEpithet());
				persisted.setSubfamily(t.getSubfamily());
				persisted.setSubgenus(t.getSubgenus());
				persisted.setSubtribe(t.getSubtribe());
				persisted.setTaxonomicStatus(t.getTaxonomicStatus());
				persisted.setTaxonRemarks(t.getTaxonRemarks());
				persisted.setTribe(t.getTribe());
				persisted.setTaxonRank(t.getTaxonRank());
				persisted.setUri(t.getUri());
				validate(t);

				replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Update);
			}

			logger.debug("Overwriting taxon {}", persisted);
			return persisted;

		}
	}

	@Override
	public void afterChunkError(ChunkContext context) { }
}
