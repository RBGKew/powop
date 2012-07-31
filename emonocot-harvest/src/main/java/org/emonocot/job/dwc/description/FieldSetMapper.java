package org.emonocot.job.dwc.description;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.emonocot.api.ReferenceService;
import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.job.dwc.taxon.CannotFindRecordException;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        DarwinCoreFieldSetMapper<TextContent> implements ChunkListener,
        StepExecutionListener {

    /**
     *
     */
    public FieldSetMapper() {
        super(TextContent.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(FieldSetMapper.class);

    /**
    *
    */
    private Parser<DateTime> dateTimeParser
        = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

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
     *
     */
    private TaxonService taxonService;

    /**
     * @param newReferenceService
     *            the referenceService to set
     */
    @Autowired
    public final void setReferenceService(
            final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     *
     * @param newTaxonService
     *            Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    @Override
    public final void mapField(final TextContent object,
            final String fieldName, final String value) throws BindException {
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to "
                + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case modified:
                if (value.length() > 0) {
                    try {
                        object.setModified(dateTimeParser.parse(value, null));
                    } catch (ParseException pe) {
                        BindException be = new BindException(object, "target");
                        be.rejectValue("modified", "not.valid", pe.getMessage());
                        throw be;
                    }
                }
                break;
            case created:
                try {
                    object.setCreated(dateTimeParser.parse(value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("created", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case source:
                object.setSource(value);
                break;
            case description:
                object.setContent(value);
                break;
            case type:
                object.setFeature(Feature.fromString(value));
                break;
            case identifier:
                object.setIdentifier(value);
                break;
            default:
                break;
            }
        }
        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case taxonID:
                try {
                    URI uri = new URI(value);
                } catch (URISyntaxException urise) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("coreId", "not.valid", urise.getMessage());
                    throw be;
                }
                Taxon taxon = taxonService.find(value);
                if (taxon == null) {
                    logger.error("Cannot find record " + value);
                    throw new CannotFindRecordException(value);
                } else {
                    object.setTaxon(taxon);
                }

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
    private void resolveReference(final TextContent object, final String value) {
        if (value == null || value.trim().length() == 0) {
            // there is not citation identifier
            return;
        } else {
            if (boundReferences.containsKey(value)) {
                object.getReferences().add(boundReferences.get(value));
            } else {
                Reference r = referenceService.findBySource(value);
                if (r == null) {
                    r = new Reference();
                    r.setIdentifier(UUID.randomUUID().toString());
                    Annotation annotation = super.createAnnotation(r,
                            RecordType.Reference, AnnotationCode.Create,
                            AnnotationType.Info);
                    r.getAnnotations().add(annotation);
                    r.getSources().add(getSource());
                    r.setAuthority(getSource());
                    r.setSource(value);
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
