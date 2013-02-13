package org.emonocot.model.convert;

import org.emonocot.api.OrganisationService;
import org.emonocot.model.registry.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToOrganisationConverter implements Converter<String, Organisation> {
	
	private Logger logger = LoggerFactory.getLogger(StringToOrganisationConverter.class);

	private OrganisationService organisationService;
	
	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}


	@Override
	public Organisation convert(String source) {
		logger.error("Convert " + source + " to organsation");
		if(source == null) {
			return null;
		}
		return organisationService.load(source);
	}

}
