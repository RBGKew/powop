package org.emonocot.job.dwc.multimedia;

import org.emonocot.api.job.TermFactory;
import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Image;
import org.emonocot.model.Multimedia;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.constants.MediaType;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author jk00kg
 *
 */
public class FieldSetMapper extends
        NonOwnedFieldSetMapper<Multimedia> {

    /**
     *
     */
    public FieldSetMapper() {
        super(Multimedia.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

   @Override
   public void mapField(Multimedia object, final String fieldName,
            final String value) throws BindException {
       super.mapField(object, fieldName, value);
        Term term = TermFactory.findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case audience:
            	object.setAudience(htmlSanitizer.sanitize(value));
            	break;
            case contributor:
            	object.setContributor(htmlSanitizer.sanitize(value));
            	break;
            case creator:
                object.setCreator(htmlSanitizer.sanitize(value));
                break;
            case description:
            	object.setDescription(htmlSanitizer.sanitize(value));
            	break;
            case format:
                object.setFormat(conversionService.convert(value, MediaFormat.class));
                break;
            case identifier:
                object.setIdentifier(value);
                break;
            case publisher:
                object.setPublisher(htmlSanitizer.sanitize(value));
                break;
            case references:
                object.setReferences(value);
                break;
            case source:
                object.setSource(value);
                break;
            case title:
                object.setTitle(htmlSanitizer.sanitize(value));
                break;
            case type:
                MediaType mediaType = conversionService.convert(value, MediaType.class);
                if (mediaType != null) {
                    switch (mediaType) {
                    case StillImage:
                    case Image:
                        object = conversionService.convert(object, Image.class);
                        break;
                    case InteractiveResource:
                    default:
                        object.setType(mediaType);
                    }
                } else {
                    logger.debug("No MediaType was provided. This should be added");
                }
                break;
            default:
                break;
            }
        }
    }
}
