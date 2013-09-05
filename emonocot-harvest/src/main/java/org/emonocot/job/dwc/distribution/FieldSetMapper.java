package org.emonocot.job.dwc.distribution;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.Location;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends OwnedEntityFieldSetMapper<Distribution> {

	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);
	
    public FieldSetMapper() {
        super(Distribution.class);
    }

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
                        addReference(object, v);
                    }
                } else {
                    addReference(object, value);
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
            	object.setEstablishmentMeans(conversionService.convert(value, EstablishmentMeans.class));
            	break;
            case locality:
            	object.setLocality(value);
            	break;
            case locationID:
            	object.setLocation(conversionService.convert(value, Location.class));
            	break;
            case occurrenceRemarks:
            	object.setOccurrenceRemarks(value);
            	break;
            case occurrenceStatus:
            	object.setOccurrenceStatus(conversionService.convert(value, OccurrenceStatus.class));
            	break;
            default:
            	break;
            }
        }
    }
    
	private void addReference(Distribution object, String value) {
		Reference reference = new Reference();
    	reference.setIdentifier(value);
        object.getReferences().add(reference);
	}
}
