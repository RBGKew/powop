package org.emonocot.job.dwc.image;

import org.emonocot.job.dwc.NonOwnedFieldSetMapper;
import org.emonocot.model.Image;
import org.emonocot.model.convert.ImageFormatConverter;
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

   
   /**
    *
    */
   private ImageFormatConverter imageFormatConverter = new ImageFormatConverter();

   

   @Override
   public void mapField(final Image object, final String fieldName,
            final String value) throws BindException {
	    super.mapField(object, fieldName, value);
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {            
            case creator:
                object.setCreator(value);
                break;
            case references:
                object.setReferences(value);
                break;
            case title:
                object.setTitle(value);
                break;
            case identifier:
                object.setIdentifier(value);                
                break;
            case format:
            	try {
                    object.setFormat(imageFormatConverter.convert(value));
                } catch (IllegalArgumentException iae) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("modified", "not.valid", iae.getMessage());
                    throw be;
                }
                break;            
            default:
                break;
            }
        }        
    }
}
