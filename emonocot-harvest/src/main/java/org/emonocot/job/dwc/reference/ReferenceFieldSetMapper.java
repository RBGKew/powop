package org.emonocot.job.dwc.reference;

import java.text.ParseException;

import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.job.dwc.taxon.CannotFindRecordException;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.reference.ReferenceType;
import org.emonocot.model.reference.ReferenceTypeConverter;
import org.emonocot.model.taxon.Taxon;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class ReferenceFieldSetMapper extends
        DarwinCoreFieldSetMapper<Reference> {

    /**
     *
     */
    public ReferenceFieldSetMapper() {
        super(Reference.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(ReferenceFieldSetMapper.class);
    /**
    *
    */
   private Parser<DateTime> dateTimeParser
       = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

   /**
    *
    */
    private Parser<ReferenceType> referenceTypeParser
        = new ReferenceTypeConverter();
    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     * @param newTaxonService Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    @Override
    public final void mapField(final Reference object, final String fieldName,
            final String value) throws BindException {
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case creator:
                object.setAuthor(value);
                break;
            case date:
                object.setDatePublished(value);
                break;
            case modified:
                try {
                    object.setModified(dateTimeParser.parse(
                            value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("modified", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case created:
                try {
                    object.setCreated(dateTimeParser.parse(
                            value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("created", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case source:
                object.setPublishedIn(value);
                break;
            case type:
                try {
                    object.setType(referenceTypeParser.parse(value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("type", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case title:
                object.setTitle(value);
                break;
            case description:
                object.setReferenceAbstract(value);
                break;
            case subject:
                object.setKeywords(value);
                break;
            case bibliographicCitation:
                object.setCitation(value);
                break;
            case identifier:
                if (value != null && value.trim().length() > 0) {
                   if (value.startsWith("http://")) {
                       object.setSource(value);
                   } else {
                       object.setIdentifier(value);
                   }
                }
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
                Taxon taxon = taxonService.find(value);
                if (taxon == null) {
                    throw new CannotFindRecordException(value);
                } else {
                    object.getTaxa().add(taxon);
                }

                break;
            default:
                break;
            }
        }

        // Gbif
        if (term instanceof GbifTerm) {
            GbifTerm gbifTerm = (GbifTerm) term;
            switch (gbifTerm) {
            default:
                break;
            }
        }

     // Unknown
        if (term instanceof UnknownTerm) {
            UnknownTerm unknownTerm = (UnknownTerm) term;
            if (unknownTerm.qualifiedName().equals(
                    "http://purl.org/ontology/bibo/volume")) {
                object.setVolume(value);
            } else if (unknownTerm.qualifiedName().equals(
                    "http://purl.org/ontology/bibo/number")) {
                object.setNumber(value);
            } else if (unknownTerm.qualifiedName().equals(
                    "http://purl.org/ontology/bibo/pages")) {
                object.setPages(value);
            } else if (unknownTerm.qualifiedName().equals(
                    "http://emonocot.org/publishedInAuthor")) {
                object.setPublishedInAuthor(value);
            } 
        }
    }
}
