package org.emonocot.job.dwc.image;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.TaxonService;
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
import org.springframework.batch.core.ChunkListener;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class ImageFieldSetMapper extends
        DarwinCoreFieldSetMapper<Image> implements ChunkListener {

    /**
     *
     */
    public ImageFieldSetMapper() {
        super(Image.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(ImageFieldSetMapper.class);

   /**
    *
    */
   private Map<String, Taxon> boundTaxa = new HashMap<String, Taxon>();

    /**
    *
    */
   private Parser<DateTime> dateTimeParser
       = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

   /**
    *
    * @param newTaxonService Set the taxon service
    */
   public final void setTaxonService(final TaxonService newTaxonService) {
       this.taxonService = newTaxonService;
   }

   /**
    *
    */
   private TaxonService taxonService;

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
            case creator:
                object.setCreator(value);
                break;
            case source:
                object.setSource(value);
                break;
            case title:
                object.setCaption(value);
                break;
            case identifier:
                object.setUrl(value);
                if(value != null && value.indexOf(".") > -1) {
                    object.setFormat(value.substring(value.lastIndexOf(".") + 1));
                }
                break;
            case format:
                object.setFormat(value);
                break;
            case license:
                object.setLicense(value);
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
                Taxon taxon = null;
                if (boundTaxa.containsKey(value)) {
                    taxon = boundTaxa.get(value);
                } else {
                    taxon = taxonService.find(value);
                    if (taxon != null) {
                        boundTaxa.put(taxon.getIdentifier(), taxon);
                    }
                }
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
     */
    public final void afterChunk() {
        logger.info("After Chunk");
    }

    /**
     *
     */
    public final void beforeChunk() {
        logger.info("Before Chunk");
        boundTaxa = new HashMap<String, Taxon>();
    }
}
