package org.emonocot.job.dwc.measurementorfact;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.constants.MeasurementUnit;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        OwnedEntityFieldSetMapper<MeasurementOrFact> implements StepExecutionListener {
	
    /**
     *
     */
    public FieldSetMapper() {
        super(MeasurementOrFact.class);
    }
    
    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

    @Override
    public final void mapField(final MeasurementOrFact object,
            final String fieldName, final String value) throws BindException {
    	super.mapField(object, fieldName, value);
    	
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to "
                + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case bibliographicCitation:
                object.setBibliographicCitation(value);
                break;
            case source:
                object.setSource(value);
                break;
            default:
                break;
            }
        }        

        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case measurementAccuracy:
            	object.setMeasurementAccuracy(value);
            	break;
            case measurementDeterminedBy:
            	object.setMeasurementDeterminedBy(value);
            	break;
            case measurementDeterminedDate:            	
            	object.setMeasurementDeterminedDate(conversionService.convert(value, DateTime.class));
            	break;
            case measurementMethod:
            	object.setMeasurementMethod(value);
            	break;
            case measurementRemarks:
            	object.setMeasurementRemarks(value);
            	break;
            case measurementType:
            	object.setMeasurementType(conversionService.convert(value, ConceptTerm.class));
            	break;
            case measurementUnit:
            	object.setMeasurementUnit(conversionService.convert(value,MeasurementUnit.class));
            	break;
            case measurementValue:
            	object.setMeasurementValue(value);
            	break;
            case measurementID:
            	object.setIdentifier(value);
            default:
            	break;
            }
        }
    }
}
