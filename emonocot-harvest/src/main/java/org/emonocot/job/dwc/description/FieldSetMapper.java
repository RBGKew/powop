package org.emonocot.job.dwc.description;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.OwnedEntityFieldSetMapper;
import org.emonocot.model.Annotation;
import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.RecordType;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        OwnedEntityFieldSetMapper<Description> implements ChunkListener,
        StepExecutionListener {

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


    /**
     *
     */
    private Map<String, Reference> boundReferences
        = new HashMap<String, Reference>();

    /**
     *
     */
    private ReferenceService referenceService;

    /**
     * @param newReferenceService
     *            the referenceService to set
     */
    @Autowired
    public final void setReferenceService(
            final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

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
                object.setDescription(value);
                break;
            case identifier:
                object.setIdentifier(value);
                break;
            case language:
                object.setLanguage(new Locale(value));
                break;
            case source:
                object.setSource(value);
                break;
            case type:
                object.setType(DescriptionType.fromString(value));
                break;            
            default:
                break;
            }
        }        

        // Unknown Terms
        if (term instanceof UnknownTerm) {
            UnknownTerm unknownTerm = (UnknownTerm) term;
            if (unknownTerm.qualifiedName().equals(
                    "http://purl.org/dc/terms/relationID")) {
                if (value.indexOf(",") != -1) {
                    String[] values = value.split(",");
                    for (String v : values) {
                        resolveReference(object, v);
                    }
                } else {
                    resolveReference(object, value);
                }
            }
        }
    }

    /**
     *
     * @param object
     *            Set the text content object
     * @param value
     *            the source of the reference to resolve
     */
    private void resolveReference(final Description object, final String value) {
        if (value == null || value.trim().length() == 0) {
            // there is not citation identifier
            return;
        } else {
            if (boundReferences.containsKey(value)) {
                object.getReferences().add(boundReferences.get(value));
            } else {
                Reference r = referenceService.find(value);
                if (r == null) {
                    r = new Reference();
                    r.setIdentifier(value);
                    Annotation annotation = super.createAnnotation(r,
                            RecordType.Reference, AnnotationCode.Create,
                            AnnotationType.Info);
                    r.getAnnotations().add(annotation);
                    r.getSources().add(getSource());
                    r.setAuthority(getSource());
                }
                boundReferences.put(value, r);
                object.getReferences().add(r);
            }
        }
    }

    /**
    *
    */
    public final void afterChunk() {
        logger.info("After Chunk");
    }

    /**
    *
    */
    public final void beforeChunk() {
        logger.info("Before Chunk");
        boundReferences = new HashMap<String, Reference>();
    }
}
