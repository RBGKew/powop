package org.emonocot.job.dwc.vernacularname;

import java.text.ParseException;
import java.util.Locale;

import org.emonocot.job.dwc.OwnedEntityFieldSetMapper;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.MeasurementType;
import org.emonocot.model.constants.MeasurementUnit;
import org.emonocot.model.geography.GeographicalRegion;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.gbif.ecat.voc.LifeStage;
import org.gbif.ecat.voc.Sex;
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
        OwnedEntityFieldSetMapper<VernacularName> implements StepExecutionListener {


	
    /**
     *
     */
    public FieldSetMapper() {
        super(VernacularName.class);
    }
    
    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

    @Override
    public final void mapField(final VernacularName object,
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
            case language:
            	object.setLanguage(new Locale(value));
            	break;
            case source:
                object.setSource(value);
                break;
            case temporal:
                object.setTemporal(value);
                break;
            default:
                break;
            }
        }        

        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case countryCode:
            	object.setCountryCode(value);
            	break;            
            case lifeStage:            	
            	object.setLifeStage(LifeStage.valueOf(value));
            	break;
            case locality:
            	object.setLocality(value);
            	break;
            case locationID:
            	object.setLocation(conversionService.convert(value, GeographicalRegion.class));
            	break;            
            case sex:
            	object.setSex(Sex.valueOf(value));
            	break;
            case taxonRemarks:
            	object.setTaxonRemarks(value);
            	break;
            case vernacularName:
            	object.setVernacularName(value);
            	break;
            default:
            	break;
            }
        }
        
        // Unknown Terms
        if (term instanceof UnknownTerm) {
            UnknownTerm unknownTerm = (UnknownTerm) term;
            if (unknownTerm.qualifiedName().equals(
                    "http://rs.gbif.org/terms/1.0/organismPart")) {
                object.setOrganismPart(value);
            } else if (unknownTerm.qualifiedName().equals(
                    "http://rs.gbif.org/terms/1.0/isPlural")) {
                object.setPlural(Boolean.valueOf(value));
            } else if (unknownTerm.qualifiedName().equals(
                    "http://rs.gbif.org/terms/1.0/isPreferredName")) {
                object.setPreferredName(Boolean.valueOf(value));
            }
        }
    }
}
