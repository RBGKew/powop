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
import org.springframework.batch.core.ChunkListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends OwnedEntityProcessor<Distribution, DistributionService> implements ChunkListener{
	
	private Map<String, Reference> boundReferences = new HashMap<String, Reference>();
		
	private ReferenceService referenceService;
	
	@Autowired
	public void setDistributionService(DistributionService service) {
		super.service = service;
	}
	
    @Autowired
    public final void setReferenceService(final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
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
   private void resolveReference(final Distribution object, final String value) {
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
   
   /**
   *
   */
   public final void afterChunk() {
       logger.info("After Chunk");
   }

   /**
   *
   */
   public final void beforeChunk() {
       logger.info("Before Chunk");
       boundReferences = new HashMap<String, Reference>();
   }
}
