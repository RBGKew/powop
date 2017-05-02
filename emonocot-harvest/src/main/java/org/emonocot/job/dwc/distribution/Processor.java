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
package org.emonocot.job.dwc.distribution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.emonocot.api.DistributionService;
import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.exception.RequiredFieldException;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.Annotation;
import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends OwnedEntityProcessor<Distribution, DistributionService> {

	private Map<String, Reference> boundReferences = new HashMap<String, Reference>();

	private ReferenceService referenceService;

	@Autowired
	public void setDistributionService(DistributionService service) {
		super.service = service;
	}

	@Autowired
	public void setReferenceService(ReferenceService referenceService) {
		this.referenceService = referenceService;
	}

	private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Override
	protected void doValidate(Distribution t) {
		if (t.getLocation() == null) {
			throw new RequiredFieldException("Distribution " + t + " at line " + getLineNumber() + " has no location set", RecordType.Distribution, getStepExecution().getReadCount());
		}
	}

	@Override
	protected void doUpdate(Distribution persisted, Distribution t) {
		persisted.setEstablishmentMeans(t.getEstablishmentMeans());
		persisted.setLocality(t.getLocality());
		persisted.setLocation(t.getLocation());
		persisted.setOccurrenceRemarks(t.getOccurrenceRemarks());
		persisted.setOccurrenceStatus(t.getOccurrenceStatus());

		persisted.getReferences().clear();
		for(Reference r : t.getReferences()) {
			resolveReference(persisted,r.getIdentifier());
		}
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Distribution;
	}

	@Override
	protected void doCreate(Distribution t) {
		Set<Reference> references = new HashSet<Reference>();
		references.addAll(t.getReferences());
		t.getReferences().clear();

		for(Reference r : references) {
			resolveReference(t,r.getIdentifier());
		}
	}

	/**
	 *
	 * @param object
	 *            Set the text content object
	 * @param value
	 *            the source of the reference to resolve
	 */
	private void resolveReference(Distribution object, String value) {
		if (value == null || value.trim().length() == 0) {
			// there is not citation identifier
			return;
		} else {
			if (boundReferences.containsKey(value)) {
				object.getReferences().add(boundReferences.get(value));
			} else {
				Reference r = referenceService.find(value);
				if (r == null) {
					r = new Reference();
					r.setIdentifier(value);
					Annotation annotation = super.createAnnotation(r,
							RecordType.Reference, AnnotationCode.Create,
							AnnotationType.Info);
					r.getAnnotations().add(annotation);
					r.setAuthority(getSource());
				}
				boundReferences.put(value, r);
				object.getReferences().add(r);
			}
		}
	}

	@Override
	public void afterChunk() {
		super.afterChunk();
		logger.debug("After Chunk");
	}

	@Override
	public void beforeChunk() {
		super.beforeChunk();
		logger.debug("Before Chunk");
		boundReferences = new HashMap<String, Reference>();
	}

	@Override
	public void beforeChunk(ChunkContext context) { }

	@Override
	public void afterChunk(ChunkContext context) { }

	@Override
	public void afterChunkError(ChunkContext context) { }
}
