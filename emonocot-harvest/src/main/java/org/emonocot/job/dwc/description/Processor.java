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
package org.emonocot.job.dwc.description;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.emonocot.api.DescriptionService;
import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.exception.RequiredFieldException;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.Annotation;
import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends OwnedEntityProcessor<Description, DescriptionService> {
	
    private Map<String, Reference> boundReferences = new HashMap<String, Reference>();
    
    private Logger logger = LoggerFactory.getLogger(Processor.class);
    
    private ReferenceService referenceService;


   @Autowired
   public void setReferenceService(ReferenceService referenceService) {
       this.referenceService = referenceService;
   }
   
    @Autowired
	public void setDescriptionService(DescriptionService service) {
		super.service = service;
	}
 
    @Override
    protected void doValidate(Description t) {
    	if (t.getType() == null) {
            throw new RequiredFieldException("Description " + t + " at line " + getLineNumber()  + " has no Feature set", RecordType.Description, getStepExecution().getReadCount());
        }
        
        if (t.getDescription() == null || t.getDescription().length() == 0) {
            throw new RequiredFieldException("Description " + t + " at line " + getLineNumber() + " has no Content set", RecordType.Description, getStepExecution().getReadCount());
        }
    }

    @Override
    protected void doUpdate(Description persisted, Description t) {
    	persisted.setAudience(t.getAudience());
    	persisted.setContributor(t.getContributor());
    	persisted.setCreator(t.getCreator());
    	persisted.setDescription(t.getDescription());
    	persisted.setLanguage(t.getLanguage());
    	persisted.setSource(t.getSource());
    	persisted.setType(t.getType());
        
        persisted.getReferences().clear();
        for(Reference r : t.getReferences()) {
            resolveReference(persisted,r.getIdentifier());
        }
    }
    
    @Override
	protected void doCreate(Description t) {
    	Set<Reference> references = new HashSet<Reference>();
    	references.addAll(t.getReferences());
    	t.getReferences().clear();
    	
        for(Reference r : references) {
            resolveReference(t,r.getIdentifier());
        }
	}

    @Override
    protected RecordType getRecordType() {
	    return RecordType.Description;
    }
    
    @Override
    public void afterChunk() {
    	super.afterChunk();
        logger.info("After Chunk");
    }
    
    /**
    *
    * @param object
    *            Set the text content object
    * @param value
    *            the source of the reference to resolve
    */
   private void resolveReference(Description object, String value) {
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
    public void beforeChunk() {
    	super.beforeChunk();
        logger.info("Before Chunk");
        boundReferences = new HashMap<String, Reference>();
    }
}
