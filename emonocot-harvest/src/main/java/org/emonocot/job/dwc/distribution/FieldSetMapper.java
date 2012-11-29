package org.emonocot.job.dwc.distribution;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.OwnedEntityFieldSetMapper;
import org.emonocot.model.Annotation;
import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.geography.GeographicalRegion;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        OwnedEntityFieldSetMapper<Distribution> implements ChunkListener, StepExecutionListener {

	private Map<String, Reference> boundReferences = new HashMap<String, Reference>();
	
	private ReferenceService referenceService;
	
    /**
     *
     */
    public FieldSetMapper() {
        super(Distribution.class);
    }
    
    @Autowired
    public final void setReferenceService(final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

    @Override
    public final void mapField(final Distribution object,
            final String fieldName, final String value) throws BindException {
    	super.mapField(object, fieldName, value);
    	
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to "
                + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {            
            case identifier:
                object.setIdentifier(value);
                break;
            case source: 
            	if (value.indexOf(",") != -1) {
                    String[] values = value.split(",");
                    for (String v : values) {
                        resolveReference(object, v);
                    }
                } else {
                    resolveReference(object, value);
                }
            default:
                break;
            }
        }        

        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case establishmentMeans:
            	object.setEstablishmentMeans(EstablishmentMeans.valueOf(value));
            	break;
            case locality:
            	object.setLocality(value);
            	break;
            case locationID:
            	object.setLocation(conversionService.convert(value, GeographicalRegion.class));
            	break;
            case occurrenceRemarks:
            	object.setOccurrenceRemarks(value);
            	break;
            case occurrenceStatus:
            	object.setOccurrenceStatus(OccurrenceStatus.valueOf(value));
            	break;
            default:
            	break;
            }
        }
        
        // Unknown Terms
        if (term instanceof UnknownTerm) {
            UnknownTerm unknownTerm = (UnknownTerm) term;

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
