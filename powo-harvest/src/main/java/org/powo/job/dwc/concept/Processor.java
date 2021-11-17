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
package org.powo.job.dwc.concept;

import java.util.HashMap;
import java.util.Map;

import org.powo.api.ConceptService;
import org.powo.api.ImageService;
import org.powo.api.ReferenceService;
import org.powo.job.dwc.read.NonOwnedProcessor;
import org.powo.model.Annotation;
import org.powo.model.Concept;
import org.powo.model.Image;
import org.powo.model.Reference;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends NonOwnedProcessor<Concept, ConceptService> implements ChunkListener {

	private Map<String, Reference> boundReferences = new HashMap<String, Reference>();

	private Map<String, Image> boundImages = new HashMap<String, Image>();

	private Logger logger = LoggerFactory.getLogger(Processor.class);

	private ReferenceService referenceService;

	private ImageService imageService;

	@Autowired
	public final void setConceptService(ConceptService conceptService) {
		super.service = conceptService;
	}

	@Autowired
	public final void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Autowired
	public final void setReferenceService(ReferenceService referenceService) {
		this.referenceService = referenceService;
	}

	@Override
	protected void doUpdate(Concept persisted, Concept t) {

		persisted.setCreator(t.getCreator());
		persisted.setAltLabel(t.getAltLabel());
		persisted.setPrefLabel(t.getPrefLabel());
		persisted.setDefinition(t.getDefinition());

		persisted.setSource(null);
		if(t.getSource() != null) {
			resolveReference(persisted,t.getSource().getIdentifier());
		}

		persisted.setPrefSymbol(null);
		if(t.getPrefSymbol() != null) {
			resolveImage(persisted,t.getPrefSymbol().getIdentifier());
		}
	}

	@Override
	protected void doPersist(Concept t) {
		if(t.getSource() != null) {
			String sourceId = t.getSource().getIdentifier();
			t.setSource(null);
			resolveReference(t,sourceId);
		}
		if(t.getPrefSymbol() != null) {
			String prefSymbolId = t.getPrefSymbol().getIdentifier();
			t.setPrefSymbol(null);
			resolveImage(t,prefSymbolId);
		}
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Concept;
	}

	@Override
	protected void bind(Concept t) {
		boundObjects.put(t.getIdentifier(), t);
	}

	@Override
	protected Concept retrieveBound(Concept t) {
		return service.find(t.getIdentifier());
	}

	@Override
	protected Concept lookupBound(Concept t) {
		return boundObjects.get(t.getIdentifier());
	}

	@Override
	protected void doValidate(Concept t) throws Exception {

	}

	@Override
	protected boolean doFilter(Concept t) {
		return false;
	}

	private void resolveReference(Concept object, String value) {
		if (value == null || value.trim().length() == 0) {
			// there is not citation identifier
			return;
		} else {
			if (boundReferences.containsKey(value)) {
				object.setSource(boundReferences.get(value));
			} else {
				Reference r = referenceService.find(value);
				if (r == null) {
					r = new Reference();
					r.setIdentifier(value);
					r.setAuthority(getSource());
				}
				boundReferences.put(value, r);
				object.setSource(r);
			}
		}
	}

	private void resolveImage(Concept object, String value) {
		if (value == null || value.trim().length() == 0) {
			// there is not image identifier
			return;
		} else {
			if (boundImages.containsKey(value)) {
				object.setPrefSymbol(boundImages.get(value));
			} else {
				Image i = imageService.find(value);
				if (i == null) {
					i = new Image();
					i.setIdentifier(value);
					i.setAuthority(getSource());
				}
				boundImages.put(value, i);
				object.setPrefSymbol(i);
			}
		}
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		super.beforeChunk(context);
		logger.debug("Before Chunk");
		boundReferences = new HashMap<String, Reference>();
		boundImages = new HashMap<String, Image>();
	}
}
