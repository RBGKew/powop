package org.emonocot.job.dwc.description;

import java.text.ParseException;

import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.NomenclaturalCode;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.RankConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class DescriptionFieldSetMapper extends
        DarwinCoreFieldSetMapper<TextContent> {

    /**
     *
     */
    public DescriptionFieldSetMapper() {
        super(TextContent.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(DescriptionFieldSetMapper.class);

    @Override
    public void mapField(final TextContent object, final String fieldName,
            final String value) throws BindException {
        // TODO Auto-generated method stub

    }
}
