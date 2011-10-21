package org.emonocot.job.dwc.image;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.job.dwc.taxon.CannotFindRecordException;
import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class ImageFieldSetMapper extends
        DarwinCoreFieldSetMapper<Image> {

    /**
     *
     */
    public ImageFieldSetMapper() {
        super(Image.class);
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(ImageFieldSetMapper.class);

    /**
    *
    */
   private Parser<DateTime> dateTimeParser
       = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());
   /**
    *
    */
   private MessageDigest messageDigest;

    @Override
    public void mapField(final Image object, final String fieldName,
            final String value) throws BindException {
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
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
                object.setSource(value);
                break;
            case title:
                object.setCaption(value);
                break;
            case identifier:
                object.setIdentifier(convertUrlToIdentifier(value));
                object.setUrl(value);
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
                    object.setTaxon(taxon);
                    object.getTaxa().add(taxon);
                }

                break;
            default:
                break;
            }
        }
    }

    /**
     *
     * @param value the string url of the image
     * @return a digested version of the url suitable for use as an identifier
     */
    private String convertUrlToIdentifier(final String value) {
        if (value == null) {
            return null;
        } else {
          return new String(messageDigest.digest(value.getBytes()));
        }
    }
}
