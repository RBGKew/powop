package org.emonocot.job.dwc.reference;

import java.util.Locale;

import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.ReferenceType;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
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
            case bibliographicCitation:
                object.setBibliographicCitation(htmlSanitizer.sanitize(value));
                break;
            case creator:
                object.setCreator(value);
                break;
            case date:
                object.setDate(value);
                break;
            case description:
                object.setDescription(value);
                break;
            case identifier:                
                object.setIdentifier(value);
                break;
            case language:
            	object.setLanguage(conversionService.convert(value, Locale.class));
            	break;
            case relation:
            	object.setUri(value);
            	break;
            case source:
                object.setSource(value);
                break;
            case subject:
                object.setSubject(value);
                break;
            case title:
                object.setTitle(value);
                break;
            case type:
                object.setType(conversionService.convert(value, ReferenceType.class));                
                break;
            default:
                break;
            }
        }
        
        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case taxonRemarks:
            	object.setTaxonRemarks(value);
            	break;
            default:
            	break;
            }
        }
    }
}
