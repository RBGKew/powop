    package org.emonocot.job.dwc.taxon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.job.dwc.exception.CannotFindRecordException;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.job.dwc.exception.TaxonAlreadyProcessedException;
import org.emonocot.model.Annotation;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class SkippingProcessor extends AuthorityAware implements ChunkListener, ItemProcessor<Taxon,Annotation> {

    private Logger logger = LoggerFactory.getLogger(SkippingProcessor.class);
    
    private TaxonService taxonService;
    
    private AnnotationService annotationService;
    
    private Map<String, Annotation> boundAnnotations = new HashMap<String,Annotation>();
    
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
        logger.info("Processing " + taxon.getIdentifier());
        if (taxon.getIdentifier() == null) {
            throw new NoIdentifierException(taxon);
        }
        Taxon persistedTaxon = taxonService.find(taxon.getIdentifier());
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
        	logger.info(persistedTaxon.getIdentifier() + " was skipped");
        }
        return annotation;
    }
    
    private Annotation resolveAnnotation(RecordType recordType, Long taxonId, Long jobExecutionId) {
    	String key = taxonId + ":" + jobExecutionId;
    	if (boundAnnotations.containsKey(key)) {
    		Annotation annotation = boundAnnotations.get(taxonId + ":" + jobExecutionId);
            logger.info("Found annotation with identifier " + taxonId + ":" + jobExecutionId + " from cache returning annotation with id " + annotation.getIdentifier());
            return annotation;
        } else {
            return annotationService.findAnnotation(recordType,taxonId, jobExecutionId);
        }
	}
    
    private void bindAnnotation(Annotation annotation) {
		boundAnnotations.put(annotation.getAnnotatedObj().getId() + ":" + getStepExecution().getJobExecutionId(), annotation);
	}

	@Override
	public void beforeChunk() {
		boundAnnotations = new HashMap<String,Annotation>();
	}

	@Override
	public void afterChunk() {
		
	}
}
