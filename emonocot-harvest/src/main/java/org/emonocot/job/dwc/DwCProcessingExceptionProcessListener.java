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
package org.emonocot.job.dwc;

import java.util.List;

import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.job.dwc.exception.CannotFindRecordException;
import org.emonocot.job.dwc.exception.DarwinCoreProcessingException;
import org.emonocot.model.Annotation;
import org.emonocot.model.Base;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class DwCProcessingExceptionProcessListener extends
        AbstractRecordAnnotator implements ItemProcessListener<Base, Base>,
        ItemReadListener<Base>, ItemWriteListener<Base> {
	
    private Logger logger = LoggerFactory.getLogger(DwCProcessingExceptionProcessListener.class);
    
    public void beforeProcess(final Base item) {

    }

    public void afterProcess(final Base item, final Base result) {

    }

    public final void onProcessError(final Base item, final Exception e) {
        logger.error("Process Error " + e.getMessage(), e);
        Annotation annotation = new Annotation();
        try {
            annotation.setRecordType(RecordType.valueOf(item.getClass().getSimpleName()));
        } catch (Exception ex) {
            annotation.setRecordType(getRecordType());
        }
        annotation.setJobId(stepExecution.getJobExecutionId());
        if (e instanceof DarwinCoreProcessingException) {
            DarwinCoreProcessingException dwcpe = (DarwinCoreProcessingException) e;
            logger.debug(dwcpe.getCode() + " | " + dwcpe.getMessage());
            annotation.setCode(dwcpe.getCode());
            annotation.setValue(dwcpe.getValue());
            annotation.setType(dwcpe.getType());
        } else {
            annotation.setCode(AnnotationCode.BadData);//TODO Replace with generic 'Something went wrong'
            annotation.setValue(stepExecution.getStepName() + " for " + 
                    item == null ? " unparsed item" : item.getIdentifier());
            annotation.setType(AnnotationType.Error);
        }
        annotation.setText(e.getMessage());
        super.annotate(annotation);
    }
    
    private RecordType getRecordType() {
    	return DwCProcessingExceptionProcessListener.stepNameToRecordType(stepExecution.getStepName());
    }

    /**
     *
     * @return the record type we are currently processing
     */
    public static RecordType stepNameToRecordType(String stepName) {
        switch(stepName) {
        case "processCoreFile":
            return RecordType.Taxon;
        case "processDescriptionFile":
            return RecordType.Description;
        case "processIdentifierFile":
            return RecordType.Identifier;
        case "processImageFile":
        case "handleBinaryImages":
            return RecordType.Image;
        case "processReferenceFile":
            return RecordType.Reference;
        case "processDistributionFile":
            return RecordType.Distribution;
        case "processMeasurementOrFactFile":
            return RecordType.MeasurementOrFact;
        case "processVernacularNameFile":
            return RecordType.VernacularName;
        case "processTypeAndSpecimenFile":
            return RecordType.TypeAndSpecimen;
        case "processKeyFile":
            return RecordType.IdentificationKey;
        default:
        	return null;
        }
    }

    public void afterRead(final Base base) {

    }

    public void beforeRead() {

    }

    public final void onReadError(final Exception e) {
    	logger.error("Read Error " + e.getMessage());
        if (e instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) e;
            StringBuffer message = new StringBuffer();
            message.append(ffpe.getMessage());
            final Annotation annotation = new Annotation();
            if (ffpe.getCause() != null) {
                message.append(" " + ffpe.getCause().getMessage());
                logger.debug("FlatFileParseException | " + ffpe.getMessage()
                        + " Cause " + ffpe.getCause().getMessage());
                if (ffpe.getCause().getClass()
                        .equals(CannotFindRecordException.class)) {
                    annotation.setCode(AnnotationCode.BadIdentifier);
                    CannotFindRecordException cfre = (CannotFindRecordException) ffpe
                            .getCause();
                    annotation.setValue(cfre.getValue());
                } else if (ffpe.getCause().getClass()
                        .equals(BindException.class)) {
                    annotation.setCode(AnnotationCode.BadField);
                    BindException be = (BindException) ffpe.getCause();
                    annotation.setValue(be.getFieldError().getField());
                } else {
                    annotation.setCode(AnnotationCode.BadRecord);
                }
            } else {
                logger.debug("FlatFileParseException | " + ffpe.getMessage());
            }

            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setRecordType(getRecordType());
            annotation.setType(AnnotationType.Error);
            annotation.setText(message.toString());
            super.annotate(annotation);
        }
    }

	@Override
	public void beforeWrite(List<? extends Base> items) {
		
	}

	@Override
	public void afterWrite(List<? extends Base> items) {
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Base> items) {
		logger.error(exception.getLocalizedMessage());
	}

}
