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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class CheckingProcessor extends AuthorityAware implements ItemProcessor<Taxon,Annotation> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(CheckingProcessor.class);
    
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
        if (taxon.getIdentifier() == null || taxon.getIdentifier().isEmpty()) {
            throw new NoIdentifierException(taxon);
        }
        Taxon persistedTaxon = taxonService.find(taxon.getIdentifier());
        if (persistedTaxon == null) {
            throw new CannotFindRecordException(taxon.getIdentifier(), taxon.toString());
        }

        Annotation annotation = annotationService.findAnnotation(RecordType.Taxon,persistedTaxon.getId(), getStepExecution().getJobExecutionId());

        if (annotation == null) {
        	logger.warn(taxon.getIdentifier() + " was not expected");
            throw new UnexpectedTaxonException(taxon);
        } else {
        	if (annotation.getCode().equals(AnnotationCode.Present)) {
                throw new TaxonAlreadyProcessedException(taxon);
            }
            annotation.setType(AnnotationType.Info);
            annotation.setCode(AnnotationCode.Present);
        	logger.info(taxon.getIdentifier() + " was expected");
        }
        return annotation;
    }
}
