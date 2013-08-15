package org.emonocot.job.dwc.concept;

import org.emonocot.api.job.SkosTerm;
import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Concept;
import org.emonocot.model.Image;
import org.emonocot.model.Reference;
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
        NonOwnedFieldSetMapper<Concept> {

    /**
     *
     */
    public FieldSetMapper() {
        super(Concept.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);   

   @Override
   public void mapField(final Concept object, final String fieldName,
            final String value) throws BindException {
	    super.mapField(object, fieldName, value);
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {            
            case creator:
                object.setCreator(htmlSanitizer.sanitize(value));
                break;            
            case identifier:
                object.setIdentifier(value);                
                break;
            case source:
            	addReference(object,value);
                break;
            default:
                break;
            }
        }    
    
		// Skos Terms
        if (term instanceof SkosTerm) {
        	SkosTerm skosTerm = (SkosTerm)term;
        	switch (skosTerm) {
        	case altLabel:
            	object.setAltLabel(htmlSanitizer.sanitize(value));
            	break;
        	case definition:
            	object.setDefinition(htmlSanitizer.sanitize(value));
            	break;
            case prefLabel:
            	object.setPrefLabel(htmlSanitizer.sanitize(value));
            	break;
            case prefSymbol:
            	addImage(object,value);
            	break;                                   
            default:
                break;
            }
        }
    }

    private void addReference(Concept object, String value) {
    	Reference reference = new Reference();
    	reference.setIdentifier(value);
        object.setSource(reference);
    }
    
    private void addImage(Concept object, String value) {
    	Image image = new Image();
    	image.setIdentifier(value);
        object.setPrefSymbol(image);
    }
}
