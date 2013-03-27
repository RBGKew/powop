package org.emonocot.job.dwc.description;

import java.util.Locale;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.DescriptionType;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends OwnedEntityFieldSetMapper<Description> implements  StepExecutionListener {

    /**
     *
     */
    public FieldSetMapper() {
        super(Description.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

    @Override
    public final void mapField(final Description object,
            final String fieldName, final String value) throws BindException {
    	super.mapField(object, fieldName, value);
    	
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to "
                + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case audience:
                object.setAudience(value);
                break;
            case creator:
                object.setCreator(value);
                break;
            case contributor:
                object.setContributor(value);
                break;
            case description:
                object.setDescription(htmlSanitizer.sanitize(value));
                break;
            case identifier:
                object.setIdentifier(value);
                break;
            case language:
                object.setLanguage(conversionService.convert(value, Locale.class));
                break;
            case source:
            	if (value.indexOf(",") != -1) {
                    String[] values = value.split(",");
                    for (String v : values) {
                        addReference(object, v);
                    }
                } else {
                	addReference(object,value);
                }
                break;
            case references:
                object.setSource(value);
                break;
            case type:
                object.setType(conversionService.convert(value, DescriptionType.class));
                break;            
            default:
                break;
            }
        }
    }

	private void addReference(Description object, String value) {
		Reference reference = new Reference();
    	reference.setIdentifier(value);
        object.getReferences().add(reference);
	}
    
}
