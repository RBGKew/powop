package org.emonocot.job.dwc.read;

import org.emonocot.model.BaseData;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.BindException;

public class BaseDataFieldSetMapper<T extends BaseData> extends DarwinCoreFieldSetMapper<T> {
	
	private Logger logger = LoggerFactory.getLogger(BaseDataFieldSetMapper.class);
	
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
		Term term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to " + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case accessRights:
            	object.setAccessRights(value);
            	break;
            case created:
                object.setCreated(conversionService.convert(value, DateTime.class));
                break;
            case license:
            	object.setLicense(value);
            	break;
            case modified:
                object.setModified(conversionService.convert(value,DateTime.class));
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
