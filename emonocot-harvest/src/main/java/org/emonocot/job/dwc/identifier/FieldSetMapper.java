package org.emonocot.job.dwc.identifier;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.Identifier;
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
public class FieldSetMapper extends OwnedEntityFieldSetMapper<Identifier> {

    /**
     *
     */
    public FieldSetMapper() {
        super(Identifier.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

    @Override
    public final void mapField(final Identifier object, final String fieldName,
            final String value) throws BindException {
    	
    	super.mapField(object, fieldName, value);
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case format:
                object.setFormat(value);
                break;
            case identifier:
                object.setIdentifier(value);
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
    }
}
