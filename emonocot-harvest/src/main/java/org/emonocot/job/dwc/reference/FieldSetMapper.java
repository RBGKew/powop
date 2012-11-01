package org.emonocot.job.dwc.reference;

import org.emonocot.job.dwc.NonOwnedFieldSetMapper;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.ReferenceType;
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
public class FieldSetMapper extends  NonOwnedFieldSetMapper<Reference> {

    /**
     *
     */
    public FieldSetMapper() {
        super(Reference.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);


    @Override
    public final void mapField(final Reference object, final String fieldName,
            final String value) throws BindException {
    	super.mapField(object, fieldName, value);
    	
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case creator:
                object.setCreator(value);
                break;
            case date:
                object.setDate(value);
                break;            
            case source:
                object.setSource(value);
                break;
            case type:
                try {
                    object.setType(ReferenceType.valueOf(value));
                } catch (IllegalArgumentException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("type", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case title:
                object.setTitle(value);
                break;
            case description:
                object.setDescription(value);
                break;
            case subject:
                object.setSubject(value);
                break;
            case bibliographicCitation:
                object.setBibliographicCitation(value);
                break;
            case identifier:                
                object.setIdentifier(value);
                break;
            default:
                break;
            }
        }
    }
}
