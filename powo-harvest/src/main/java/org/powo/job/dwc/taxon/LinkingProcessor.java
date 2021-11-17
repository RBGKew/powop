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

import org.powo.api.TaxonService;
import org.powo.harvest.service.TaxonPersistedService;
import org.powo.job.dwc.exception.CannotFindRecordException;
import org.powo.job.dwc.exception.NoIdentifierException;
import org.powo.job.dwc.read.DarwinCoreProcessor;
import org.powo.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkingProcessor extends DarwinCoreProcessor<Taxon> {
	private Logger logger = LoggerFactory.getLogger(LinkingProcessor.class);

	/**
	 * We use a {@link TaxonPersistedService} here rather than {@link TaxonService} so that changes to
	 * the taxon are not immediately flushed. Hibernate presumably does this to avoid consistency issues
	 * but since we just overwrite the persisted taxon and don't read any details from it 
	 */
	@Autowired
	private TaxonPersistedService taxonService;

	@Override
	public Taxon doProcess(Taxon taxon) throws Exception {
		if (taxon.getIdentifier() == null || taxon.getIdentifier().isEmpty()) {
			throw new NoIdentifierException(taxon);
		}
		var persistedTaxon = taxonService.find(taxon.getIdentifier());

		if (persistedTaxon == null) {
			throw new CannotFindRecordException(taxon.getIdentifier());
		}

		linkRecords(taxon, persistedTaxon);

		persistedTaxon.setCreated(taxon.getCreated());
		persistedTaxon.setModified(taxon.getModified());
		persistedTaxon.setTaxonomicStatus(taxon.getTaxonomicStatus());
		persistedTaxon.setAuthority(getSource());
		persistedTaxon.addAuthorityToTaxonAndRelatedTaxa(getSource());
		persistedTaxon.setClazz(taxon.getClazz());
		persistedTaxon.setBibliographicCitation(taxon.getBibliographicCitation());

		return persistedTaxon;
	}

	private Taxon linkRecords(Taxon taxon, Taxon persistedTaxon) throws Exception {
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

}