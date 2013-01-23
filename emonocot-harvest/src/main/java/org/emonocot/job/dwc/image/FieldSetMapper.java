package org.emonocot.job.dwc.image;

import org.emonocot.api.job.Wgs84Term;
import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Image;
import org.emonocot.model.constants.ImageFormat;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        NonOwnedFieldSetMapper<Image> {

    /**
     *
     */
    public FieldSetMapper() {
        super(Image.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);   

   @Override
   public void mapField(final Image object, final String fieldName,
            final String value) throws BindException {
	    super.mapField(object, fieldName, value);
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case audience:
            	object.setAudience(value);
            	break;
            case contributor:
            	object.setContributor(value);
            	break;
            case creator:
                object.setCreator(value);
                break;
            case description:
            	object.setDescription(value);
            	break;
            case format:
                object.setFormat(conversionService.convert(value, ImageFormat.class));
                break;
            case identifier:
                object.setIdentifier(value);                
                break;
            case publisher:
                object.setPublisher(value);                
                break;
            case references:
                object.setReferences(value);
                break;
            case spatial:
                object.setSpatial(value);                
                break;
            case subject:
                object.setSubject(value);                
                break;
            case title:
                object.setTitle(value);
                break;                                    
            default:
                break;
            }
        }    
    
		// WGS84 Terms
        if (term instanceof Wgs84Term) {
        	Wgs84Term wgs84Term = (Wgs84Term)term;
        	switch (wgs84Term) {
            case latitude:
            	object.setLatitude(conversionService.convert(value, Double.class));
            	break;
            case longitude:
            	object.setLongitude(conversionService.convert(value, Double.class));
            	break;                                   
            default:
                break;
            }
        }
    }
}
