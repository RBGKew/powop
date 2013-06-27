    package org.emonocot.job.dwc.taxon;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.job.dwc.exception.CannotFindRecordException;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.job.dwc.exception.TaxonAlreadyProcessedException;
import org.emonocot.job.dwc.exception.UnexpectedTaxonException;
import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.registry.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class SkippingProcessor extends AuthorityAware implements ItemProcessor<Taxon,Annotation> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(SkippingProcessor.class);
    
    private TaxonService taxonService;
    
    private AnnotationService annotationService;
    
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

        Annotation annotation = annotationService.findAnnotation(RecordType.Taxon,persistedTaxon.getId(), getStepExecution().getJobExecutionId());

        if (annotation == null) {
        	annotation = this.createAnnotation(persistedTaxon, RecordType.Taxon, AnnotationCode.Skipped, AnnotationType.Info);
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
}
