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

import java.util.HashMap;
import java.util.Map;

import org.powo.api.AnnotationService;
import org.powo.harvest.common.AuthorityAware;
import org.powo.harvest.service.PersistedService;
import org.powo.job.dwc.exception.CannotFindRecordException;
import org.powo.job.dwc.exception.NoIdentifierException;
import org.powo.job.dwc.exception.TaxonAlreadyProcessedException;
import org.powo.model.Annotation;
import org.powo.model.Taxon;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class SkippingProcessor extends AuthorityAware implements ChunkListener, ItemProcessor<Taxon,Annotation> {

	private Logger logger = LoggerFactory.getLogger(SkippingProcessor.class);

	@Autowired
	private PersistedService<Taxon> taxonService;

	/**
	 * @param taxon a taxon object
	 * @throws Exception if something goes wrong
	 * @return Taxon a taxon object
	 */
	public final Annotation process(final Taxon taxon) throws Exception {
		logger.debug("Processing " + taxon.getIdentifier());
		if (taxon.getIdentifier() == null) {
			throw new NoIdentifierException(taxon);
		}
		Taxon persistedTaxon = taxonService.find(taxon.getIdentifier());
		if (persistedTaxon == null) {
			throw new CannotFindRecordException(taxon.getIdentifier());
		}
		return null;
	}

	@Override
	public void beforeChunk(ChunkContext context) {
	}

	@Override
	public void afterChunk(ChunkContext context) {}

	@Override
	public void afterChunkError(ChunkContext context) { }
}
