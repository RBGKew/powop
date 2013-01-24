package org.emonocot.job.dwc.vernacularname;

import java.util.Locale;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.Location;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
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
            	object.setLocation(conversionService.convert(value, Location.class));
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
        
        // Gbif Terms
        if (term instanceof GbifTerm) {
            GbifTerm gbifTerm = (GbifTerm) term;
            switch(gbifTerm) {
            case organismPart:
            	object.setOrganismPart(value);
            	break;
            case isPlural:
            	object.setPlural(conversionService.convert(value, Boolean.class));
            case isPreferredName:
            	object.setPreferredName(conversionService.convert(value, Boolean.class));
            default:
            	break;
            }            
        }
    }
}
