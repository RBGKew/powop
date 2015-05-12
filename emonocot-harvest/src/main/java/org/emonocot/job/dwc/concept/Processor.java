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
package org.emonocot.job.dwc.concept;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.ConceptService;
import org.emonocot.api.ImageService;
import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.read.NonOwnedProcessor;
import org.emonocot.model.Annotation;
import org.emonocot.model.Concept;
import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is slightly different from the description validator because we believe
 * (or at least, I believe) that descriptive content is somehow "owned" or
 * "contained in" the taxon i.e. that if the taxon does not exist, the
 * descriptive content doesn't either. There is a one-to-many relationship
 * between taxa and descriptive content because there can be many facts about
 * each taxon, but each fact is only about one taxon.
 *
 * Images, on the other hand, have an inherently many-to-many relationship with
 * taxa as one image can appear on several different taxon pages (especially the
 * family page, type genus page, type species page etc). Equally a taxon page
 * can have many images on it. So Images don't belong to any one taxon page
 * especially. If we delete the taxon, the image can hang around - it might have
 * value on its own.
 *
 * @author ben
 *
 */
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
	                   Annotation annotation = super.createAnnotation(r,
	                           RecordType.Reference, AnnotationCode.Create,
	                           AnnotationType.Info);
	                   r.getAnnotations().add(annotation);
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
	                   Annotation annotation = super.createAnnotation(i,
	                           RecordType.Image, AnnotationCode.Create,
	                           AnnotationType.Info);
	                   i.getAnnotations().add(annotation);
	                   i.setAuthority(getSource());
	               }
	               boundImages.put(value, i);
	               object.setPrefSymbol(i);
	           }
	       }
	   }

	@Override
	public void beforeChunk() {
		super.beforeChunk();
        logger.info("Before Chunk");
        boundReferences = new HashMap<String, Reference>();
        boundImages = new HashMap<String, Image>();
    }
}
