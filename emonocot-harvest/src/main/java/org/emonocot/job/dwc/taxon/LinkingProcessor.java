
package org.emonocot.job.dwc.taxon;
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

import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.exception.CannotFindRecordException;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.job.dwc.read.DarwinCoreProcessor;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkingProcessor extends DarwinCoreProcessor<Taxon> {
	private Logger logger = LoggerFactory.getLogger(CheckingProcessor.class);

	private TaxonService taxonService;

	private Taxon persistedTaxon;

	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	@Override
	public Taxon doProcess(Taxon taxon) throws Exception {
		if (taxon.getIdentifier() == null || taxon.getIdentifier().isEmpty()) {
			throw new NoIdentifierException(taxon);
		}
		persistedTaxon = taxonService.find(taxon.getIdentifier());

		if (persistedTaxon == null) {
			throw new CannotFindRecordException(taxon.getIdentifier(), taxon.toString());
		} else {
			linkRecords(taxon);
			persistedTaxon.setCreated(taxon.getCreated());
			persistedTaxon.setModified(taxon.getModified());
			persistedTaxon.setTaxonomicStatus(taxon.getTaxonomicStatus());
			persistedTaxon.setAuthority(getSource());
			persistedTaxon.setClazz(taxon.getClazz());
			persistedTaxon.setBibliographicCitation(taxon.getBibliographicCitation());
			persistedTaxon.setTaxonRemarks(taxon.getTaxonRemarks());
			persistedTaxon.getAnnotations().add(createAnnotation(persistedTaxon, RecordType.Taxon, AnnotationCode.Update, AnnotationType.Info));
		}

		return persistedTaxon;
	}

	private Taxon linkRecords(Taxon taxon) throws Exception {
		if (taxon.getParentNameUsage() != null) {
			logger.debug("setting {} as a child of {}",  taxon.getIdentifier(), taxon.getParentNameUsage().getIdentifier());
			persistedTaxon.setParentNameUsage(taxonService.find(taxon.getParentNameUsage().getIdentifier()));
		} else {
			logger.debug("clearing parent name usage of {} ", persistedTaxon.getIdentifications());
			persistedTaxon.setParentNameUsage(null);
		}

		if (taxon.getAcceptedNameUsage() != null) {
			logger.debug("setting {} as a synonym of {}", taxon.getIdentifier(), taxon.getAcceptedNameUsage().getIdentifier());
			persistedTaxon.setAcceptedNameUsage(taxonService.find(taxon.getAcceptedNameUsage().getIdentifier()));
		} else {
			logger.debug("clearing accepted name usage of {} ", persistedTaxon.getIdentifications());
			persistedTaxon.setAcceptedNameUsage(null);
		}

		if (taxon.getOriginalNameUsage() != null) {
			logger.debug("setting {} as an orginal name of {}", taxon.getIdentifier(), taxon.getOriginalNameUsage().getIdentifier());
			persistedTaxon.setOriginalNameUsage(taxonService.find(taxon.getOriginalNameUsage().getIdentifier()));
		} else {
			logger.debug("clearing original name usage of {} ", persistedTaxon.getIdentifications());
			persistedTaxon.setOriginalNameUsage(null);
		}

		return taxon;
	}

	@Override
	public void afterChunkError(ChunkContext context) { }
}