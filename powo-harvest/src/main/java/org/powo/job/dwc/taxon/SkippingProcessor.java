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
import org.powo.api.TaxonService;
import org.powo.harvest.common.AuthorityAware;
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

	private TaxonService taxonService;

	private AnnotationService annotationService;

	private Map<String, Annotation> boundAnnotations = new HashMap<String,Annotation>();

	private int chunkCount = 0;

	private long chunkStart;

	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	@Autowired
	public void setAnnotationService(AnnotationService annotationService) {
		this.annotationService = annotationService;
	}

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
		Taxon persistedTaxon = taxonService.findPersisted(taxon.getIdentifier());
		if (persistedTaxon == null) {
			throw new CannotFindRecordException(taxon.getIdentifier(), taxon.toString());
		}

		Annotation annotation = resolveAnnotation(RecordType.Taxon,persistedTaxon.getId(), getStepExecution().getJobExecutionId());

		if (annotation == null) {
			annotation = this.createAnnotation(persistedTaxon, RecordType.Taxon, AnnotationCode.Skipped, AnnotationType.Info);
			bindAnnotation(annotation);
		} else {
			if (annotation.getCode().equals(AnnotationCode.Skipped)) {
				throw new TaxonAlreadyProcessedException(taxon);
			}

			annotation.setType(AnnotationType.Info);
			annotation.setCode(AnnotationCode.Skipped);
			logger.debug(persistedTaxon.getIdentifier() + " was skipped");
		}
		return annotation;
	}

	private Annotation resolveAnnotation(RecordType recordType, Long taxonId, Long jobExecutionId) {
		String key = taxonId + ":" + jobExecutionId;
		if (boundAnnotations.containsKey(key)) {
			Annotation annotation = boundAnnotations.get(taxonId + ":" + jobExecutionId);
			logger.debug("Found annotation with identifier " + taxonId + ":" + jobExecutionId + " from cache returning annotation with id " + annotation.getIdentifier());
			return annotation;
		} else {
			return annotationService.findAnnotation(recordType,taxonId, jobExecutionId);
		}
	}

	private void bindAnnotation(Annotation annotation) {
		boundAnnotations.put(annotation.getAnnotatedObj().getId() + ":" + getStepExecution().getJobExecutionId(), annotation);
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		boundAnnotations = new HashMap<String,Annotation>();
		chunkStart = System.currentTimeMillis();
	}

	@Override
	public void afterChunk(ChunkContext context) {
		chunkCount++;
		var millis = System.currentTimeMillis() - chunkStart;
		logger.info("Chunk " + chunkCount + " took " + millis + " millis");
	}

	@Override
	public void afterChunkError(ChunkContext context) { }
}
