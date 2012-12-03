package org.emonocot.job.dwc.measurementorfact;

import java.text.ParseException;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.constants.MeasurementType;
import org.emonocot.model.constants.MeasurementUnit;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
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
            case identifier:
                object.setIdentifier(value);
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
            	try {
                    object.setMeasurementDeterminedDate(dateTimeParser.parse(value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("measurementDeterminedDate", "not.valid", pe.getMessage());
                    throw be;
                }
            	break;
            case measurementMethod:
            	object.setMeasurementMethod(value);
            	break;
            case measurementRemarks:
            	object.setMeasurementRemarks(value);
            	break;
            case measurementType:
            	object.setMeasurementType(MeasurementType.valueOf(value));
            	break;
            case measurementUnit:
            	object.setMeasurementUnit(MeasurementUnit.valueOf(value));
            	break;
            case measurementValue:
            	object.setMeasurementValue(value);
            	break;
            default:
            	break;
            }
        }
    }
}
