package org.emonocot.job.dwc;

import java.text.ParseException;

import org.emonocot.model.BaseData;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

public class BaseDataFieldSetMapper<T extends BaseData> extends DarwinCoreFieldSetMapper<T> {
	
	private Logger logger = LoggerFactory.getLogger(BaseDataFieldSetMapper.class);

	protected Parser<DateTime> dateTimeParser = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());
	
	protected ConversionService conversionService;
	
	public BaseDataFieldSetMapper(Class<T> newType) {
		super(newType);		
	}
	
    @Autowired
    public final void setConversionService(ConversionService conversionService) {
    	this.conversionService = conversionService;
    }

	@Override
	public void mapField(T object, String fieldName, String value)
			throws BindException {
		ConceptTerm term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to " + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case accessRights:
            	object.setAccessRights(value);
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
            case license:
            	object.setLicense(value);
            	break;
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
            case rights:
            	object.setRights(value);
            	break;
            case rightsHolder:
            	object.setRightsHolder(value);
            	break;
            default:
                break;
            }
        }        
    }
}
